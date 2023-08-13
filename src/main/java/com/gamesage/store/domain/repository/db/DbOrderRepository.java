package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class DbOrderRepository implements Repository<Order, Integer> {

	private static final String INSERT_ORDER = "INSERT INTO orders (user_id, game_id, order_datetime) " +
			"VALUES (?, ?, ?) ";
	private static final String SELECT_ACTIVE_ORDERS_QUERY =
			"SELECT orders.id AS id, user_id, game_id, order_datetime, " +
					" user.login, user.password, user.tier_id, user.balance, " +
					" game.id, game.name, game.price" +
					" FROM orders " +
					" INNER JOIN user ON  user.id = user_id " +
					" INNER JOIN game ON game.id = game_id ";
	private static final String SELECT_ORDER_QUERY =
			"SELECT orders.id AS id, user_id, game_id, order_datetime, " +
					" user.login, user.password, user.tier_id, user.balance, " +
					" game.id, game.name, game.price " +
					" FROM orders " +
					" LEFT JOIN user ON  user.id = user_id " +
					" LEFT JOIN game ON game.id = game_id " +
					" WHERE orders.id = ?";
	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<Order> rowMapper;

	public DbOrderRepository(JdbcTemplate jdbcTemplate, RowMapper<Order> rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public Order createOne(Order orderToAdd) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(con -> {
			PreparedStatement ps = con
					.prepareStatement(INSERT_ORDER,
							Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, orderToAdd.getUserId());
			ps.setInt(2, orderToAdd.getGameId());
			ps.setTimestamp(3, Timestamp.valueOf(orderToAdd.getDateTime()));
			return ps;
		}, keyHolder);
		return new Order(keyHolder.getKeyAs(Integer.class),
				orderToAdd.getUser(),
				orderToAdd.getGame(),
				orderToAdd.getDateTime());
	}

	@Override
	public Optional<Order> findById(Integer id) {
		try {
			return Optional.ofNullable(jdbcTemplate.queryForObject(
					SELECT_ORDER_QUERY
					, rowMapper
					, id));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Order> findAll() {
		return jdbcTemplate.query(SELECT_ACTIVE_ORDERS_QUERY, rowMapper);
	}

	@Component
	static class OrderRowMapper implements RowMapper<Order> {

		@Override
		public Order mapRow(ResultSet rs, int rowNum) throws SQLException {

			Tier tier = new Tier(
					rs.getInt("tier_id"),
					null,
					0);

			User user = new User(
					rs.getInt("user_id"),
					rs.getString("login"),
					rs.getString("password"),
					tier,
					rs.getBigDecimal("balance")
			);

			Game game = new Game(
					rs.getInt("game_id"),
					rs.getString("name"),
					rs.getBigDecimal("price"));

			Timestamp timestamp = rs.getTimestamp("order_datetime");
			LocalDateTime dateTime = timestamp.toLocalDateTime();

			return new Order(
					rs.getInt("id"),
					user,
					game,
					dateTime);
		}
	}
}


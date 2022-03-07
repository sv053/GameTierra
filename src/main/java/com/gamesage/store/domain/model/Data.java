package com.gamesage.store.domain.model;

import java.time.LocalDate;

public class Data {

    private final String name;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String nick;
    private final Long phone;
    private final String address;
    private final String comment;
    private final LocalDate birthDate;

    public Data(Builder builder) {
        this.name = builder.name;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
        this.nick = builder.nick;
        this.phone = builder.phone;
        this.address = builder.address;
        this.comment = builder.comment;
        this.birthDate = builder.birthDate;
    }

    public static class Builder {

        private final String name;
        private final String firstName;
        private final String middleName;
        private final String lastName;
        private final String nick;
        private Long phone;
        private String address;
        private String comment;
        private LocalDate birthDate;

        public Builder(String firstName, String middleName, String lastName, String nick) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.nick = nick;
            this.name = firstName + " " + middleName + " " + lastName;
        }

        public Builder phone(Long phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Data build() {
            return new Data(this);
        }
    }
}

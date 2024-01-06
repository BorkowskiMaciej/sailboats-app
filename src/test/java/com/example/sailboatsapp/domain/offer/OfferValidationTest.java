package com.example.sailboatsapp.domain.offer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OfferValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    public void whenAllFieldsValid_thenNoConstraintViolations() {
        Offer offer = createValidOffer();

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenPortBlank_thenConstraintViolation() {
        Offer offer = createValidOffer();
        offer.setPort("");

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("port"));
    }

    @Test
    public void whenPriceNegative_thenConstraintViolation() {
        Offer offer = createValidOffer();
        offer.setPrice(-1);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("price"));
    }

    @Test
    public void whenDepositNegative_thenConstraintViolation() {
        Offer offer = createValidOffer();
        offer.setDeposit(-1);

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("deposit"));
    }

    @Test
    public void whenDescriptionBlank_thenConstraintViolation() {
        Offer offer = createValidOffer();
        offer.setDescription("");

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    @Test
    public void whenDescriptionTooLong_thenConstraintViolation() {
        Offer offer = createValidOffer();
        offer.setDescription("a".repeat(5001));

        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    private Offer createValidOffer() {
        return Offer.builder()
                .id(1L)
                .boatId(1L)
                .ownerId(1L)
                .port("Valid Port")
                .price(500)
                .deposit(200)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(10))
                .description("Valid description")
                .build();
    }
}

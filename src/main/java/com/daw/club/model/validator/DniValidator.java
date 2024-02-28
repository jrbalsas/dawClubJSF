package com.daw.club.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Sample Spanish DNI Custom validator*/
public class DniValidator implements ConstraintValidator<Dni, String> {

    private final Logger logger= Logger.getLogger(DniValidator.class.getName());
    @Override
    public boolean isValid(String dni, ConstraintValidatorContext context) {

        String regExDni="(\\d{8})-?([a-zA-Z])";

        Matcher matcher = Pattern.compile(regExDni).matcher(dni);

        logger.info("Validando dni " + dni);
        if (!matcher.matches()) {
            //incorrect DNI format
            return false; //default format message
        }

        String numeroDni = matcher.group(1);
        String letraDni = matcher.group(2);

        // Calculate DNI checksum letter
        int resto = Integer.parseInt(numeroDni) % 23;
        String letra = "TRWAGMYFPDXBNJZSQVHLCKE";

        // Verify DNI validation letter
        boolean dniValido=letra.charAt(resto) == letraDni.charAt(0);

        if (!dniValido) {
            //Invalid DNI letter
            //Custom error validation message
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El dni %s no es válido".formatted(dni))
                    .addConstraintViolation();
        }

        return dniValido;
    }

}

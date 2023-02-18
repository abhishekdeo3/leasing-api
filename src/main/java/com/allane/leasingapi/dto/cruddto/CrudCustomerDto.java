package com.allane.leasingapi.dto.cruddto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class CrudCustomerDto {

   private String firstName;

   private String lastName;

   private Date birthdate;
}

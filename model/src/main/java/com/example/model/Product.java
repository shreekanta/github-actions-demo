package com.example.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {
	private String id;
	private String type;
	private String name;
	private String version;
}

package com.man.OrderService.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		return mapper;
	}

//	@Autowired
//	private ModelMapper mapper;
//
//	@PostConstruct
//	public void init() {
//		mapper.typeMap(CartItemResponse.class, Product.class)
//				.addMappings(m -> m.map(CartItemResponse::getProductId, Product::setId));
//
//	}
}

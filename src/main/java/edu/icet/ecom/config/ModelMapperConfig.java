package edu.icet.ecom.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.icet.ecom.dto.OrderItemDTO;
import edu.icet.ecom.entity.OrderItemEntity;

import org.modelmapper.Converter;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        // Try to ignore ambiguity globally before creating type maps
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        Converter<OrderItemEntity, OrderItemDTO> orderItemConverter = context -> {
            OrderItemEntity source = context.getSource();
            OrderItemDTO destination = new OrderItemDTO();

            destination.setId(source.getId());
            destination.setMenuItemId(source.getMenuItemId());
            destination.setName(source.getName());
            destination.setImage(source.getImage());
            destination.setPrice(source.getPrice());
            destination.setQuantity(source.getQuantity());

            return destination;
        };

        modelMapper.createTypeMap(OrderItemEntity.class, OrderItemDTO.class)
                .setConverter(orderItemConverter); // Set the custom converter for this TypeMap

        return modelMapper;
    }
}

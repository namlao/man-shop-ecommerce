package com.man.CartService.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.man.CartService.client.ProductClient;
import com.man.CartService.client.UserClient;
import com.man.CartService.entity.Cart;
import com.man.CartService.entity.CartItem;
import com.man.CartService.repository.CartRepository;
import com.man.CartService.repository.ItemCartRepository;
import com.man.CartService.request.AddProductToCartRequest;
import com.man.CartService.request.CartCreateRequest;
import com.man.CartService.request.DeleteProductToCartRequest;
import com.man.CartService.response.AddProductToCartResponse;
import com.man.CartService.response.CartCreateResponse;
import com.man.CartService.response.CartItemResponse;
import com.man.CartService.response.DeleteProductToCartResponse;
import com.man.CartService.response.GetByIdResponse;
import com.man.CartService.service.CartService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository repository;
	@Autowired
	private ItemCartRepository itemCartRepository;

	@Autowired
	private ProductClient productClient;

	@Autowired
	private UserClient userClient;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CartCreateResponse createCart(CartCreateRequest cartReq) {
		Cart cart = mapper.map(cartReq, Cart.class);

		List<CartItem> cartItems = cartReq.getItems().stream().map(itemReq -> {
			CartItem item = mapper.map(itemReq, CartItem.class);
			item.setCart(cart);
			Long subTotal = productClient.getById(item.getProductId()).getPrice() * item.getQuantity();
			item.setSubTotal(subTotal);
			return item;
		}).toList();

		cart.setItems(cartItems);

		Long total = cartItems.stream().mapToLong(CartItem::getSubTotal).sum();
		cart.setTotal(total);

		Cart saved = repository.save(cart);

		List<CartItemResponse> itemResponses = saved.getItems().stream()
				.map(item -> mapper.map(item, CartItemResponse.class)).toList();

		CartCreateResponse response = mapper.map(saved, CartCreateResponse.class);
		response.setItems(itemResponses);
		return response;
	}

	@Override
	public AddProductToCartResponse addProductToCart(AddProductToCartRequest itemReqs) {

		Cart cart = repository.findById(itemReqs.getId()).orElseThrow(() -> new RuntimeException("Cart not found"));

		List<CartItem> cartItems = itemReqs.getItems().stream().map(item -> {
			Long subTotal = productClient.getById(item.getProductId()).getPrice() * item.getQuantity();
			item.setSubTotal(subTotal);
			return mapper.map(item, CartItem.class);
		}).toList();

		Long total = cart.getTotal();

		for (CartItem item : cartItems) {
			CartItem oldCartItem = itemCartRepository.findByCartIdAndProductId(cart.getId(), item.getProductId())
					.orElse(null);
			if (oldCartItem != null) {
				System.out.println("Tìm thấy productId: Quanity cũ: "+ oldCartItem.getQuantity() +" Quanity req: "+item.getQuantity());
				Long newQuantity = oldCartItem.getQuantity() + item.getQuantity();
				oldCartItem.setQuantity(newQuantity);
				
				System.out.println("Tìm thấy productId: Giá cũ: "+ oldCartItem.getSubTotal() +"Giá req: "+item.getSubTotal());
				Long newSubTotal = oldCartItem.getSubTotal() + item.getSubTotal();
				oldCartItem.setSubTotal(newSubTotal);
				
				total += item.getQuantity() * productClient.getById(oldCartItem.getProductId()).getPrice();
			}else {
				System.out.println("không có productId");
				CartItem newItem = mapper.map(item, CartItem.class);
				Long subNewTotal = productClient.getById(newItem.getProductId()).getPrice() * newItem.getQuantity();
	            newItem.setSubTotal(subNewTotal);
	            newItem.setCart(cart);

	            cart.addCartItem(newItem);
	            total += subNewTotal;
				
			}
		}
		cart.setTotal(total);
	    cart = repository.save(cart);

		return mapper.map(cart, AddProductToCartResponse.class);
	}

	@Override
	@Transactional
	public DeleteProductToCartResponse removeProductFromCart(DeleteProductToCartRequest cartReq) {
		// Lấy cart từ DB
		Cart cart = repository.findById(cartReq.getCartId()).orElseThrow(() -> new RuntimeException("Cart not found"));

		// Lấy ra danh sách productId có trong cartReq
		List<Long> removeProuctId = cartReq.getItems().stream().map(CartItem::getProductId)
				.collect(Collectors.toList());

		// tính tổng total product trong cartReq
		Long removeTotal = cart.getItems().stream().filter(item -> removeProuctId.contains(item.getProductId()))
				.mapToLong(CartItem::getSubTotal).sum();

		// xóa productId trong cart
		cart.getItems().removeIf(item -> removeProuctId.contains(item.getProductId()));

		// cập nhật lại total
		cart.setTotal(cart.getTotal() - removeTotal);

		// lưu db
		Cart saveCart = repository.save(cart);

		// Trả về response
		DeleteProductToCartResponse response = mapper.map(saveCart, DeleteProductToCartResponse.class);
		response.setItems(saveCart.getItems().stream().map(item -> mapper.map(item, CartItemResponse.class))
				.collect(Collectors.toList()));

		return response;
	}

	@Override
	public void clearCart(Long cartId) {
		// TODO Auto-generated method stub
		Cart cart = repository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));

		repository.delete(cart);

	}

	@Override
	public List<Cart> getCartByUserId(Long userId) {
		// TODO Auto-generated method stub
		return repository.findByUserId(userId);
	}

	@Override
	public GetByIdResponse getById(Long id) {
		Cart cart = repository.findById(id).orElseThrow(()-> new RuntimeException("Cart Not Found"));

		
		return mapper.map(cart, GetByIdResponse.class);
	}

}

package pl.edu.wszib.springwithtests;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.dozer.Mapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.edu.wszib.springwithtests.dao.ProductDao;
import pl.edu.wszib.springwithtests.dao.ShoppingBasketDao;
import pl.edu.wszib.springwithtests.dao.ShoppingBasketItemDao;
import pl.edu.wszib.springwithtests.dto.ProductDTO;
import pl.edu.wszib.springwithtests.dto.ShoppingBasketDTO;
import pl.edu.wszib.springwithtests.model.Product;
import pl.edu.wszib.springwithtests.model.ShoppingBasket;
import pl.edu.wszib.springwithtests.model.ShoppingBasketItem;
import pl.edu.wszib.springwithtests.model.Vat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringMvcTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ShoppingBasketDao shoppingBasketDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    ShoppingBasketItemDao shoppingBasketItemDao;
    @Autowired
    Mapper mapper;

    @Test
    public void testShoppingBasketExistProductNotExist() throws Exception {
        int testBasketId = 3125;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(354);
        productDTO.setCost(26d);
        productDTO.setVat(Vat.VALUE_8);


        mockMvc
                .perform(MockMvcRequestBuilders.post("/shoppingBasket/add")
                        .contentType("application/json")
                        .content(new Gson().toJson(productDTO))
                        .param("shoppingBasketId", String.valueOf(testBasketId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testShoppingBasketExistProductExist() throws Exception {
        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket = shoppingBasketDao.save(shoppingBasket);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(354);
        productDTO.setName("testowy produkt");
        productDTO.setCost(26d);
        productDTO.setVat(Vat.VALUE_8);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/shoppingBasket/add")
                        .contentType("application/json")
                        .content(new Gson().toJson(productDTO))
                        .param("shoppingBasketId", String.valueOf(shoppingBasket.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
    @Test
    public void testShppingBasketExistProduktExistShoppingBasketItemNotExist() throws Exception {

        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket = shoppingBasketDao.save(shoppingBasket);

        Product product = new Product();
        product.setId(354);
        product.setName("testowy produkt");
        product.setCost(26d);
        product.setVat(Vat.VALUE_8);
        product = productDao.save(product);

        ShoppingBasketItem shoppingBasketItem = new ShoppingBasketItem();
        shoppingBasketItem.setProduct(product);
        shoppingBasketItem.setShoppingBasket(shoppingBasket);
        shoppingBasketItem.setAmount(1);
        shoppingBasketItem = shoppingBasketItemDao.save(shoppingBasketItem);

        ProductDTO productDTO = mapper.map(product, ProductDTO.class);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/shoppingBasket/add")
                        .contentType("application/json")
                        .content(new Gson().toJson(productDTO))
                        .param("shoppingBasketId", String.valueOf(shoppingBasket.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        ShoppingBasketDTO shoppingBasketDTO = new Gson()
                .fromJson(result.getResponse()
                        .getContentAsString(),
                        ShoppingBasketDTO.class);
        Assert.assertEquals(shoppingBasket.getId(), shoppingBasketDTO.getId());


    }@Test
    public void testShppingBasketExistProduktExistShoppingBasketItemNotExist2() throws Exception {

        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket = shoppingBasketDao.save(shoppingBasket);

        Product product = new Product();
        product.setId(354);
        product.setName("testowy produkt");
        product.setCost(26d);
        product.setVat(Vat.VALUE_8);
        product = productDao.save(product);


        ProductDTO productDTO = mapper.map(product, ProductDTO.class);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/shoppingBasket/add")
                        .contentType("application/json")
                        .content(new Gson().toJson(productDTO))
                        .param("shoppingBasketId", String.valueOf(shoppingBasket.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        ShoppingBasketDTO shoppingBasketDTO = new Gson()
                .fromJson(result.getResponse()
                        .getContentAsString(),
                        ShoppingBasketDTO.class);
        Assert.assertEquals(shoppingBasket.getId(), shoppingBasketDTO.getId());


    }

}

//package com.increff.assure.dto;
//
//import com.increff.assure.spring.AbstractUnitTest;
//import model.data.ChannelInvoiceData;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//
//
//import static com.increff.assure.spring.TestPojo.*;
//import static org.junit.Assert.assertEquals;
//
//public class OrderDtoTest extends AbstractUnitTest {
//
//
//    @InjectMocks
//    private OrderDto dto;
//
//
//
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//
//    @Test
//    public void testGetInvoice() throws Exception {
//        ChannelInvoiceData data = createChannelInvoiceData("puma", "flipkart", "dhruwanth",
//                "2022-04-01", 1L,100.0);
//        String s = dto.generateInvoice(data);
//
//        assertEquals(s.length()>0, true);
//    }
//
//}
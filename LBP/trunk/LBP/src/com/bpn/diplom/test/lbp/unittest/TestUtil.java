package com.bpn.diplom.test.lbp.unittest;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bpn.diplom.test.lbp.Util;

public class TestUtil {
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	 
	 
	 
	@Test
	public void testIsLDPCode(){
		assertTrue("isLDPCode wrong work", Util.isLDPCode("000000"));
		assertTrue("isLDPCode wrong work", Util.isLDPCode("111111"));
		assertTrue("isLDPCode wrong work", Util.isLDPCode("001111"));
		assertTrue("isLDPCode wrong work", Util.isLDPCode("111000"));
		assertTrue("isLDPCode wrong work", Util.isLDPCode("001110"));
		assertTrue("isLDPCode wrong work", Util.isLDPCode("111001"));
		assertTrue("isLDPCode wrong work", Util.isLDPCode("100000"));
		assertTrue("isLDPCode wrong work", Util.isLDPCode("000001"));
		assertTrue("isLDPCode wrong work", Util.isLDPCode("1000001"));
		assertFalse("isLDPCode wrong work", Util.isLDPCode("0101010"));
		assertFalse("isLDPCode wrong work", Util.isLDPCode("0010010"));
		assertFalse("isLDPCode wrong work", Util.isLDPCode("10001001"));
		assertFalse("isLDPCode wrong work", Util.isLDPCode("0101110"));
	}
	
	@Test
	public void testIntInBin(){
		Assert.assertEquals("111111111", Util.intInBin(511));
		Assert.assertEquals("", Util.intInBin(0));
		Assert.assertEquals("100000000", Util.intInBin(256));
		Assert.assertEquals("111", Util.intInBin(7));
		Assert.assertEquals("110", Util.intInBin(6));
		Assert.assertEquals("101", Util.intInBin(5));
		Assert.assertEquals("1010", Util.intInBin(10));
		Assert.assertEquals("111111110", Util.intInBin(510));
		
	}
	
	
//	 @Test
//	 public void verifyGoodZipCode() throws Exception{		
//	  Matcher mtcher = this.pattern.matcher("22101");
//	  boolean isValid = mtcher.matches();		
//	  assertTrue("Pattern did not validate zip code", isValid);
//	 }
}

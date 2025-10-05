package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "nartTxtBoxSeg")
@DocumentObject("narr")
@Data
public class NartTxtBoxSegment {

	private String textBox1;

	private String textBox2;

	private String textBox3;

	private String textBox4;

	private String textBox5;

	private String textBox6;

	private String textBox7;

	private String textBox8;

	private String textBox9;

	private String textBox10;

	private String textBox11;

	private String textBox12;

	private String textBox13;

	private String textBox14;

	private String textBox15;
}

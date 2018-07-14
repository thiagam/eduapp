package org.nsna;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsna.NSNAEduApplication;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NSNAEduApplication.class)
@WebAppConfiguration
public class NSNAEduApplicationTests {

	@Test
	public void contextLoads() {
	}

}

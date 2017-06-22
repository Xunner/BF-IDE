package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import serviceImpl.ExecuteServiceImpl;

@RunWith(Parameterized.class)
public class ExecuteServiceImplTest {
	private ExecuteServiceImpl esi;
	private String result;
	private String code;
	private String param;

	public ExecuteServiceImplTest(String result, String code, String param) {
		this.esi = new ExecuteServiceImpl();
		this.result = result;
		this.code = code;
		this.param = param;
	}
	
	@Parameters
	public static Collection<Object[]> data(){
		return Arrays.asList(new Object[][]{
			{"X", ",.", "X"},
			{"123", ",[.,]", "123\0"},
			{"7", ",>++++++[<-------->-],,[<+>-],<.", "4+3\n"},
			{"HELLOWORLD", ",----------[----------------------.,----------]", "helloworld\n"},
			{"Hello World!", "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.", ""},
			{"I LOVE YOU", "++++++++[>+++++++++<-]>+.[-]++++[>++++++++<-]>.++++++[>++<-]>.+++.+++++++.-----------------.[-]++++[>++++++++<-]>.--[>+++<-]>-.----------.++++++.", ""}
		});
	}
	
	@Test
	public void testExecute() {
		try {
			assertEquals(result, esi.BFExecute(code, param));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}

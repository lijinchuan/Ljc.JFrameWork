package Core;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Ljc.JFramework.TypeUtil.DateTime;
import Ljc.JFramework.Utility.Tuple;

public class PersionService extends Ljc.JFramework.SOA.ESBService {

	public PersionService() throws Exception {
		super(1000, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object DoResponse(Tuple<Integer, byte[]> tp) throws Exception {
		int funcId = tp.GetItem1();
		byte[] Param = tp.GetItem2();
		switch (funcId) {
		case 1: {
			GetPersonRequest request = Ljc.JFramework.EntityBufCore.DeSerialize(GetPersonRequest.class, Param);
			if (request.getId() == 9999) {
				GetPersonResponse resp = new GetPersonResponse();
				resp.setId(9999);

				NewPersonInfo info = new NewPersonInfo() {
					@SuppressWarnings("deprecation")
					@Override
					public NewPersonInfo Fill() {
						setAge(30);
						try {
							setBirth(DateTime.ParseDateTime("1986-12-30 16:30:12"));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						setName("李金川");
						this.setFriends(new String[] { "张三", "李四", "angilar baby", "陈距" });

						HashMap<String, NewPersonInfo> finfos = new HashMap<String, NewPersonInfo>();
						finfos.put("张三", new NewPersonInfo() {
							@Override
							public NewPersonInfo Fill() {
								this.setAge(20);
								try {
									this.setBirth(DateTime.ParseDate("1982-10-10"));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								this.setName("张三");
								this.setIdNo("421182198612301310");
								this.setFriends(new String[] { "李世民", "刘邦" });
								List<String> list = new LinkedList<String>();
								list.add("东方小学");
								list.add("汉口中学");
								list.add("深圳大学");

								this.setSchools(list);
								return this;
							}
						}.Fill());
						this.setFriendsInfo(finfos);
						return this;
					}
				}.Fill();
				resp.setInfo(info);
				return resp;
			} else {
				return null;
			}
		}
		default: {
			return super.DoResponse(new Tuple<Integer, byte[]>(funcId, Param));
		}
		}
	}

}

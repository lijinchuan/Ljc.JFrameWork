package Core;

import java.text.ParseException;

import Ljc.JFramework.TypeUtil.DateTime;

public class PersionService extends Ljc.JFramework.SOA.ESBService {

	public PersionService() throws Exception {
		super(1000);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object DoResponse(int funcId, byte[] Param) throws Exception {
		switch (funcId) {
		case 1: {
			GetPersonRequest request = Ljc.JFramework.EntityBufCore.DeSerialize(GetPersonRequest.class, Param, true);
			if (request.getId() == 9999) {
				GetPersonResponse resp = new GetPersonResponse();
				resp.setId(9999);

				NewPersonInfo info = new NewPersonInfo() {
					@SuppressWarnings("deprecation")
					@Override
					public void Fill() {
						setAge(30);
						try {
							setBirth(DateTime.ParseDateTime("1986-12-30 16:30:12"));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						setName("Àî½ð´¨");
					}
				};
				info.Fill();
				resp.setInfo(info);
				return resp;
			} else {
				return null;
			}
		}
		default: {
			return super.DoResponse(funcId, Param);
		}
		}
	}

}

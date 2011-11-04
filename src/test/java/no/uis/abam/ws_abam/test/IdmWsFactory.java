package no.uis.abam.ws_abam.test;

import no.uis.service.idm.ws.IdmWebService;

import static org.easymock.EasyMock.*;
import org.springframework.beans.factory.FactoryBean;

public class IdmWsFactory implements FactoryBean<IdmWebService> {

  @Override
  public IdmWebService getObject() throws Exception {
    IdmWebService mock = createMock(IdmWebService.class);
    //expect(mock.)
    replay(mock);
    return mock;
  }

  @Override
  public Class<?> getObjectType() {
    return IdmWebService.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }
}

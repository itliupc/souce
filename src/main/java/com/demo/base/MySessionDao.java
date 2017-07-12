package com.demo.base;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;

public class MySessionDao extends CachingSessionDAO {

  /**
   * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
   */
  @Override
  protected void doUpdate(Session session) {
    System.out.println("doUpdate ******************************");
  }

  @Override
  protected void doDelete(Session session) {
    System.out.println("doDelete ******************************");
  }

  @Override
  protected Serializable doCreate(Session session) {
    System.out.println("doCreate ******************************");
    Serializable sessionId = generateSessionId(session);
    this.assignSessionId(session, sessionId);
    return sessionId;
  }

  @Override
  protected Session doReadSession(Serializable sessionId) {
    System.out.println("doReadSession ******************************");
    Session cached = this.getCachedSession(sessionId);;
    return cached;
  }
  
}

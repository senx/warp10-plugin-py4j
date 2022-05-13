//
//   Copyright 2018-2022  SenX S.A.S.
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
//

package io.warp10.plugins.py4j;

import java.net.InetAddress;
import java.util.Properties;

import javax.net.ServerSocketFactory;

import io.warp10.Py4JEntryPoint;
import io.warp10.continuum.Configuration;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import io.warp10.warp.sdk.AbstractWarp10Plugin;
import py4j.GatewayServer;
import py4j.Py4JGatewayServer;
import py4j.Py4JPythonClient;

public class Py4JWarp10Plugin extends AbstractWarp10Plugin {

  public static final String CONFIG_PY4J_PREFIX = "py4j";
  public static final String CONFIG_PY4J_HOST = CONFIG_PY4J_PREFIX + ".host";
  public static final String CONFIG_PY4J_PORT = CONFIG_PY4J_PREFIX + ".port";
  public static final String CONFIG_PY4J_AUTHTOKEN = CONFIG_PY4J_PREFIX + ".authtoken";
  public static final String CONFIG_PY4J_TIMEOUT_READ = CONFIG_PY4J_PREFIX + ".timeout.read";
  public static final String CONFIG_PY4J_TIMEOUT_CONNECT = CONFIG_PY4J_PREFIX + ".timeout.connect";

  public static final String CONFIG_PY4J_USE_SSL = CONFIG_PY4J_PREFIX + ".use.ssl";
  public static final String CONFIG_PY4J_SSL_KEYSTORE_PATH = CONFIG_PY4J_PREFIX + Configuration._SSL_KEYSTORE_PATH;
  public static final String CONFIG_PY4J_SSL_CERT_ALIAS = CONFIG_PY4J_PREFIX + Configuration._SSL_CERT_ALIAS;
  public static final String CONFIG_PY4J_SSL_KEYSTORE_PASSWORD = CONFIG_PY4J_PREFIX + Configuration._SSL_KEYSTORE_PASSWORD;
  public static final String CONFIG_PY4J_SSL_KEYMANAGER_PASSWORD = CONFIG_PY4J_PREFIX + Configuration._SSL_KEYMANAGER_PASSWORD;

  private SslContextFactory getSslContextFactory(Properties props) throws Exception {
    if (null == props.getProperty(CONFIG_PY4J_SSL_KEYSTORE_PATH)) {
      throw new RuntimeException(CONFIG_PY4J_SSL_KEYSTORE_PATH + " is required but not set.");
    }

    if (null == props.getProperty(CONFIG_PY4J_SSL_CERT_ALIAS)) {
      throw new RuntimeException(CONFIG_PY4J_SSL_CERT_ALIAS + " is required but not set.");
    }

    if (null == props.getProperty(CONFIG_PY4J_SSL_KEYSTORE_PASSWORD)) {
      throw new RuntimeException(CONFIG_PY4J_SSL_KEYSTORE_PASSWORD + " is required but not set.");
    }

    SslContextFactory sslContextFactory = new SslContextFactory();
    sslContextFactory.setKeyStorePath(props.getProperty(CONFIG_PY4J_SSL_KEYSTORE_PATH));
    sslContextFactory.setCertAlias(props.getProperty(CONFIG_PY4J_SSL_CERT_ALIAS));
    sslContextFactory.setKeyStorePassword(props.getProperty(CONFIG_PY4J_SSL_KEYSTORE_PASSWORD));

    if (null != props.getProperty(CONFIG_PY4J_SSL_KEYMANAGER_PASSWORD)) {
      sslContextFactory.setKeyManagerPassword(props.getProperty(CONFIG_PY4J_SSL_KEYMANAGER_PASSWORD));
    }

    sslContextFactory.start();
    return sslContextFactory;
  }

  @Override
  public void init(Properties props) {
    
    String host = props.getProperty(CONFIG_PY4J_HOST, GatewayServer.DEFAULT_ADDRESS);
    int port = Integer.parseInt(props.getProperty(CONFIG_PY4J_PORT, Integer.toString(GatewayServer.DEFAULT_PORT)));
    int readTimeout = Integer.parseInt(props.getProperty(CONFIG_PY4J_TIMEOUT_READ, Integer.toString(GatewayServer.DEFAULT_READ_TIMEOUT)));
    int connectTimeout = Integer.parseInt(props.getProperty(CONFIG_PY4J_TIMEOUT_CONNECT, Integer.toString(GatewayServer.DEFAULT_CONNECT_TIMEOUT)));

    String authToken = props.getProperty(CONFIG_PY4J_AUTHTOKEN);

    try {

      ServerSocketFactory ssf;
      if ("true".equals(props.getProperty(CONFIG_PY4J_USE_SSL))) {
        ssf = getSslContextFactory(props).getSslContext().getServerSocketFactory();

      } else {
        ssf = ServerSocketFactory.getDefault();
      }

      InetAddress addr = InetAddress.getByName(host);

      // Callbacks are not supported
      Py4JPythonClient cb = new io.warp10.plugins.py4j.Py4JPythonClient(); // new CallbackClient(pyport, pyaddr, CallbackClient.DEFAULT_MIN_CONNECTION_TIME, TimeUnit.SECONDS, sslContext.getSocketFactory());

      GatewayServer gateway = new Py4JGatewayServer(new Py4JEntryPoint(), port, addr, connectTimeout, readTimeout, null, cb, ssf, authToken);
      gateway.start();      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

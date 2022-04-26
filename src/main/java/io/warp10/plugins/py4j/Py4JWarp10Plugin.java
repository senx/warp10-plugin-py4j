//
//   Copyright 2018-22  SenX S.A.S.
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
import java.net.UnknownHostException;
import java.util.Properties;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import io.warp10.Py4JEntryPoint;
import io.warp10.continuum.Configuration;
import io.warp10.sensision.jarjar.org.eclipse.jetty.util.ssl.SslContextFactory;
import io.warp10.warp.sdk.AbstractWarp10Plugin;
import py4j.GatewayServer;
import py4j.Py4JGatewayServer;
import py4j.Py4JPythonClient;

public class Py4JWarp10Plugin extends AbstractWarp10Plugin {

  public static final String CONFIG_PY4J_PREFIX = "py4j";
  public static final String CONFIG_PY4J_HOST = CONFIG_PY4J_PREFIX + ".host";
  public static final String CONFIG_PY4J_PORT = CONFIG_PY4J_PREFIX + ".port";
  public static final String CONFIG_PY4J_USE_SSL = CONFIG_PY4J_PREFIX + ".use.ssl";
  public static final String CONFIG_PY4J_AUTHTOKEN = CONFIG_PY4J_PREFIX + ".authtoken";
  public static final String CONFIG_PY4J_PYTHON_PORT = CONFIG_PY4J_PREFIX + ".python.port";
  public static final String CONFIG_PY4J_PYTHON_HOST = CONFIG_PY4J_PREFIX + ".python.host";
  public static final String CONFIG_PY4J_TIMEOUT_READ = CONFIG_PY4J_PREFIX + ".timeout.read";
  public static final String CONFIG_PY4J_TIMEOUT_CONNECT = CONFIG_PY4J_PREFIX + ".timeout.connect";
  public static final String CONFIG_PY4J_STACK_NOLIMITS = CONFIG_PY4J_PREFIX + ".stack.nolimits";
  public static final String CONFIG_PY4J_WARPSCRIPT_PYTHON = CONFIG_PY4J_PREFIX + ".warpscript.python";

  private SslContextFactory getSslContextFactory(Properties props, String prefix) {
    SslContextFactory sslContextFactory = new SslContextFactory();
    sslContextFactory.setKeyStorePath(props.getProperty(prefix + Configuration._SSL_KEYSTORE_PATH));
    sslContextFactory.setCertAlias(props.getProperty(prefix + Configuration._SSL_CERT_ALIAS));

    if (null != props.getProperty(prefix + Configuration._SSL_KEYSTORE_PASSWORD)) {
      sslContextFactory.setKeyStorePassword(props.getProperty(prefix + Configuration._SSL_KEYSTORE_PASSWORD));
    }
    if (null != props.getProperty(prefix + Configuration._SSL_KEYMANAGER_PASSWORD)) {
      sslContextFactory.setKeyManagerPassword(props.getProperty(prefix + Configuration._SSL_KEYMANAGER_PASSWORD));
    }

    return sslContextFactory;
  }

  @Override
  public void init(Properties props) {
    
    String host = props.getProperty(CONFIG_PY4J_HOST, GatewayServer.DEFAULT_ADDRESS);
    String pyhost = props.getProperty(CONFIG_PY4J_PYTHON_HOST, GatewayServer.DEFAULT_ADDRESS);
    int port = Integer.parseInt(props.getProperty(CONFIG_PY4J_PORT, Integer.toString(GatewayServer.DEFAULT_PORT)));
    int readTimeout = Integer.parseInt(props.getProperty(CONFIG_PY4J_TIMEOUT_READ, Integer.toString(GatewayServer.DEFAULT_READ_TIMEOUT)));
    int connectTimeout = Integer.parseInt(props.getProperty(CONFIG_PY4J_TIMEOUT_CONNECT, Integer.toString(GatewayServer.DEFAULT_CONNECT_TIMEOUT)));
    int pyport = Integer.parseInt(props.getProperty(CONFIG_PY4J_PYTHON_PORT, Integer.toString(GatewayServer.DEFAULT_PYTHON_PORT)));

    String authToken = props.getProperty(CONFIG_PY4J_AUTHTOKEN);

    try {

      ServerSocketFactory ssf;
      if ("true".equals(props.getProperty(CONFIG_PY4J_USE_SSL))) {
        if (null == props.getProperty(CONFIG_PY4J_PREFIX + Configuration._SSL_KEYSTORE_PATH) && null == props.getProperty(CONFIG_PY4J_PREFIX + Configuration._SSL_CERT_ALIAS)) {
          ssf = SSLServerSocketFactory.getDefault();
        } else {
          ssf = getSslContextFactory(props, CONFIG_PY4J_PREFIX).getSslContext().getServerSocketFactory();
        }
      } else {
        ssf = ServerSocketFactory.getDefault();
      }

      InetAddress addr = InetAddress.getByName(host);      
      InetAddress pyaddr = InetAddress.getByName(pyhost);
     
      Py4JPythonClient cb = null; // new CallbackClient(pyport, pyaddr);
      
      //if (!"true".equals(props.getProperty(CONFIG_PY4J_WARPSCRIPT_PYTHON))) {
        cb = new io.warp10.plugins.py4j.Py4JPythonClient();
      //}
      GatewayServer gateway = new Py4JGatewayServer(new Py4JEntryPoint(), port, addr, connectTimeout, readTimeout, null, cb, ssf, authToken);
      gateway.start();      
    } catch (UnknownHostException uhe) {
      throw new RuntimeException(uhe);
    }
  }
}

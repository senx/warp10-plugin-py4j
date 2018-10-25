//
//   Copyright 2018  SenX S.A.S.
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

import io.warp10.Py4JEntryPoint;
import io.warp10.warp.sdk.AbstractWarp10Plugin;
import py4j.GatewayServer;
import py4j.Py4JGatewayServer;
import py4j.GatewayServer.GatewayServerBuilder;
import py4j.Py4JPythonClient;

public class Py4JWarp10Plugin extends AbstractWarp10Plugin {
    
  public static final String CONFIG_PY4J_HOST = "py4j.host";
  public static final String CONFIG_PY4J_PORT = "py4j.port";
  public static final String CONFIG_PY4J_AUTHTOKEN = "py4j.authtoken";
  public static final String CONFIG_PY4J_PYTHON_PORT = "py4j.python.port";
  public static final String CONFIG_PY4J_PYTHON_HOST = "py4j.python.host";
  public static final String CONFIG_PY4J_TIMEOUT_READ = "py4j.timeout.read";
  public static final String CONFIG_PY4J_TIMEOUT_CONNECT = "py4j.timeout.connect";
  public static final String CONFIG_PY4J_STACK_NOLIMITS = "py4j.stack.nolimits";
  public static final String CONFIG_PY4J_WARPSCRIPT_PYTHON = "py4j.warpscript.python";
  
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
      InetAddress addr = InetAddress.getByName(host);      
      InetAddress pyaddr = InetAddress.getByName(pyhost);
     
      Py4JPythonClient cb = null; // new CallbackClient(pyport, pyaddr);
      
      //if (!"true".equals(props.getProperty(CONFIG_PY4J_WARPSCRIPT_PYTHON))) {
        cb = new io.warp10.plugins.py4j.Py4JPythonClient();
      //}
      GatewayServer gateway = new Py4JGatewayServer(new Py4JEntryPoint(), port, addr, connectTimeout, readTimeout, null, cb, ServerSocketFactory.getDefault(), authToken);
      gateway.start();      
    } catch (UnknownHostException uhe) {
      throw new RuntimeException(uhe);
    }
  }
}

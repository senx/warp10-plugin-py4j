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

package io.warp10.plugins.pywarp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import io.warp10.warp.sdk.AbstractWarp10Plugin;
import py4j.CallbackClient;
import py4j.GatewayServer;
import py4j.Py4JPythonClient;

public class PyWarpWarp10Plugin extends AbstractWarp10Plugin {
    
  public static final String CONFIG_PYWARP_HOST = "pywarp.host";
  public static final String CONFIG_PYWARP_PORT = "pywarp.port";
  public static final String CONFIG_PYWARP_PYTHON_PORT = "pywarp.python.port";
  public static final String CONFIG_PYWARP_PYTHON_HOST = "pywarp.python.host";
  public static final String CONFIG_PYWARP_TIMEOUT_READ = "pywarp.timeout.read";
  public static final String CONFIG_PYWARP_TIMEOUT_CONNECT = "pywarp.timeout.connect";
  public static final String CONFIG_PYWARP_STACK_NOLIMITS = "pywarp.stack.nolimits";
  public static final String CONFIG_PYWARP_WARPSCRIPT_PYTHON = "pywarp.warpscript.python";
  
  @Override
  public void init(Properties props) {
    
    String host = props.getProperty(CONFIG_PYWARP_HOST, GatewayServer.DEFAULT_ADDRESS);
    String pyhost = props.getProperty(CONFIG_PYWARP_PYTHON_HOST, GatewayServer.DEFAULT_ADDRESS);
    int port = Integer.parseInt(props.getProperty(CONFIG_PYWARP_PORT, Integer.toString(GatewayServer.DEFAULT_PORT)));
    int readTimeout = Integer.parseInt(props.getProperty(CONFIG_PYWARP_TIMEOUT_READ, Integer.toString(GatewayServer.DEFAULT_READ_TIMEOUT)));
    int connectTimeout = Integer.parseInt(props.getProperty(CONFIG_PYWARP_TIMEOUT_CONNECT, Integer.toString(GatewayServer.DEFAULT_CONNECT_TIMEOUT)));
    int pyport = Integer.parseInt(props.getProperty(CONFIG_PYWARP_PYTHON_PORT, Integer.toString(GatewayServer.DEFAULT_PYTHON_PORT)));
    boolean nolimits = "true".equals(props.getProperty(CONFIG_PYWARP_STACK_NOLIMITS));

    try {
      InetAddress addr = InetAddress.getByName(host);      
      InetAddress pyaddr = InetAddress.getByName(pyhost);
     
      Py4JPythonClient cb = null; // new CallbackClient(pyport, pyaddr);
      
      //if (!"true".equals(props.getProperty(CONFIG_PYWARP_WARPSCRIPT_PYTHON))) {
        cb = new PyWarpPythonClient();
      //}
      //GatewayServer gateway = new GatewayServer(new PyWarpEntryPoint(nolimits), port, addr, connectTimeout, readTimeout, null, cb);
      GatewayServer gateway = new PyWarpGatewayServer(new PyWarpEntryPoint(nolimits), port, addr, connectTimeout, readTimeout, null, cb);
      gateway.start();      
    } catch (UnknownHostException uhe) {
      throw new RuntimeException(uhe);
    }
  }
}

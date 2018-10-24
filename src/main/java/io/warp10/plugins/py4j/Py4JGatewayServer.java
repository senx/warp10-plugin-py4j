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
import java.util.List;

import py4j.GatewayServer;
import py4j.Py4JPythonClient;
import py4j.commands.Command;

public class Py4JGatewayServer extends GatewayServer {
  
  public Py4JGatewayServer(Object entrypoint, int port, InetAddress addr, int connectTimeout, int readTimeout,List<Class<? extends Command>> commands, Py4JPythonClient cb) {
    super(entrypoint, port, addr, connectTimeout, readTimeout, commands, cb);
  }
  
  @Override
  public void shutdown() {
    // We ignore shutdown requests
    return;
  }
  
  @Override
  public void shutdown(boolean shutdownCallbackClient) {
    // We simply shutdown the callback client, but we do not
    // shut ourself down
    if (shutdownCallbackClient) {
      this.getCallbackClient().shutdown();
    }
    return;
  }    
}

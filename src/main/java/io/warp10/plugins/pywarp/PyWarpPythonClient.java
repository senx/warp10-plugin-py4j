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

import py4j.Gateway;
import py4j.Py4JPythonClient;

public class PyWarpPythonClient implements Py4JPythonClient {
  @Override
  public Py4JPythonClient copyWith(InetAddress pythonAddress, int pythonPort) {
    return null;
  }
  @Override
  public InetAddress getAddress() {
    return null;
  }
  @Override
  public int getPort() {
    return 0;
  }
  @Override
  public Object getPythonServerEntryPoint(Gateway gateway, Class[] interfacesToImplement) {
    return null;
  }
  @Override
  public int getReadTimeout() {
    return 0;
  }
  @Override
  public boolean isMemoryManagementEnabled() {
    return false;
  }
  @Override
  public String sendCommand(String command) {
    throw new RuntimeException("WarpScript to Python calls are unsupported (command was '" + command + "')");
  }
  @Override
  public String sendCommand(String command, boolean blocking) {
    throw new RuntimeException("WarpScript to Python calls are unsupported (command was '" + command + "')");
  }
  @Override
  public void shutdown() {    
  }
}

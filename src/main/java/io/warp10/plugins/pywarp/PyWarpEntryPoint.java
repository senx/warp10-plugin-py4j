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

import java.util.Properties;

import io.warp10.script.MemoryWarpScriptStack;
import io.warp10.script.WarpScriptStack;

public class PyWarpEntryPoint {
  
  private final boolean nolimits;
  
  public PyWarpEntryPoint(boolean nolimits) {
    this.nolimits = nolimits;
  }
  
  public WarpScriptStack newStack() {
    MemoryWarpScriptStack stack = new MemoryWarpScriptStack(PyWarpWarp10Plugin.getExposedStoreClient(), PyWarpWarp10Plugin.getExposedDirectoryClient(), new Properties());
    if (this.nolimits) {
      stack.maxLimits();
    }
    return stack;
  }
}

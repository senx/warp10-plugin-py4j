## Warp 10™ Py4J plugin

This is a Warp 10 plugin that enables to use Python to interact with the platform.

You can get it with [Warpfleet](https://warpfleet.senx.io/browse/io.warp10/warp10-plugin-py4j).

Make sure to set these configurations:

```
# register the plugin
warp10.plugin.py4j = io.warp10.plugins.py4j.Py4JWarp10Plugin
# register that there are dependencies to load if any
plugin.defaultcl.io.warp10.plugins.py4j.Py4JWarp10Plugin = true
# set to true to allow to find/fetch data through py4j
egress.clients.expose = true
```

More details [here](https://warp10.io/content/03_Documentation/04__Tooling/03_Python).

### Python example

```python
from py4j.java_gateway import JavaGateway
from py4j.java_gateway import GatewayParameters

params = GatewayParameters('127.0.0.1', 25333, auto_convert=True, auth_token="your-token")
gateway = JavaGateway(gateway_parameters=params)
stack = gateway.entry_point.newStack()
stack.execMulti('"Hello Warp 10"')
stack.pop()
```

We also made a tutorial on how to use this plugin on our [blog](https://blog.senx.io/the-py4j-plugin-for-warp-10/).

### SSL/TLS example

Since revision 1.0.2 it is also possible to use SSL/TLS encryption.

You will need a key pair with certificate, for example, you can generate them with:

`keytool -genkey -alias some-alias -keyalg RSA -storepass some-password -keystore path/to/keystore.jks`

Then, set these configuration parameters:

```
py4j.use.ssl = true
# In case previous configuration is true, the following must be set
py4j.ssl.keystore.path = path/to/keystore.jks
py4j.ssl.keystore.password = some-password
py4j.ssl.cert.alias = some-alias
```

In python, you will then have to pass a `ssl.SSLContext` object to `GatewayParameters`:

```python
import ssl

client_ssl_context = ssl.SSLContext(ssl.PROTOCOL_TLS_CLIENT)
client_ssl_context.check_hostname = False # you can set this to True if the client loads a certification chain and you specify a hostname in GatewayParameters
client_ssl_context.verify_mode = ssl.CERT_NONE # the client won't check the certification chain as we trust the server self-certificate since we generated it
params = GatewayParameters('127.0.0.1', 25333, auto_convert=True, auth_token="your-token", ssl_context=client_ssl_context)
gateway = JavaGateway(gateway_parameters=params)

# The communication is TLS encrypted
...
```
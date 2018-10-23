#!/usr/bin/python

from py4j.java_gateway import JavaGateway
from py4j.java_gateway import GatewayParameters

# @see https://www.py4j.org/advanced_topics.html#collections-conversion
gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_convert=True))
#gateway = JavaGateway()

stack = gateway.entry_point.newStack()

stack.push(42)
#stack.push([ 1,2,3,4 ])
stack.push('foo')
stack.execMulti('SNAPSHOT')
print stack.pop()

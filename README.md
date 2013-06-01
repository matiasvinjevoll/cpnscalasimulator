cpnscalasimulator
=================

A Coloured Petri Net simulator implemented in Scala as part of my master's thesis.

This repository includes:

- The Scala CPN simulator (CPNSimulator)

- Transformation of models (CPNTransformation) from Access/CPN

- Code generation of CPN models (CPNCodeGeneration)

- CPN model of the Babel Routing protocol (http://www.pps.univ-paris-diderot.fr/~jch/software/babel/), using both CPN ML and Scala as inscription language (BabelCPNModel)

CPNSimulator, CPNTransformation, CPNCodeGeneration are Eclipse projects.

CPNTransformation and CPNCodeGeneration are dependent on AccessCPN (http://cpntools.org/accesscpn/start).
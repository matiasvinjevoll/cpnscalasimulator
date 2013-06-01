package cpnmodel.babel.impl

abstract class TLV

case class HelloTLV(seqno: Int) extends TLV

case class IHUTLV(rxcost: Int) extends TLV

case class UpdateTLV(routerId: Int, seqno: Int, metric: Int) extends TLV

case class RouteReqTLV(routerId: Int) extends TLV

case class SeqNoReqTLV(seqno: Int, hopCount: Int, routerId: Int) extends TLV
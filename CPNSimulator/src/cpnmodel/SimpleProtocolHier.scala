package cpnmodel

import model.Transition
import model.CPNGraph
import model.Arc
import model.Guard
import model.Place
import model.Pattern
import model.BindingElement
import simulator.Simulator
import model.BindGuard
import model.ArcPattern
import model.EvalGuard
import model.Direction._
import simulator.Conversions._
import scala.collection.immutable.Queue
import simulator.CollectionExtensions._
import collection.Multiset
import scala.collection.generic.CanBuildFrom

object SimpleProtocolHier extends App {
  val net = CPNGraph()
  // Declarations
  case class Data(n: Int, d: String)
  case class Ack(n: Int)
  case class No(n: Int) {
    def next = No(n)
  }
  // ########################################################################
  // Generated code for module Top
  // ########################################################################
  class Top() {
    val net = CPNGraph()
    // Places
    val id44 = Place("id44", "Packets To Send", Multiset(
      (1, Data(1, "COL")),
      (1, Data(2, "OUR")),
      (1, Data(3, "ED ")),
      (1, Data(4, "PET")),
      (1, Data(5, "RI ")),
      (1, Data(6, "NET"))))
    net.addPlace(id44)
    val id71 = Place("id71", "Received", Multiset((1, "")))
    net.addPlace(id71)
    val id58 = Place("id58", "D", Multiset[Ack]())
    net.addPlace(id58)
    val id93 = Place("id93", "C", Multiset[Ack]())
    net.addPlace(id93)
    val id87 = Place("id87", "B", Multiset[Data]())
    net.addPlace(id87)
    val id64 = Place("id64", "A", Multiset[Data]())
    net.addPlace(id64)
    // Wire modules
    val st0 = new Receiver(id87, id93, id71)
    net.addSubstitutionTransition(st0.net)
    val st1 = new Network(id64, id87, id58, id93)
    net.addSubstitutionTransition(st1.net)
    val st2 = new Sender(id44, id64, id58)
    net.addSubstitutionTransition(st2.net)
  }
  val top0 = new Top()
  net.addSubstitutionTransition(top0.net)
  // ########################################################################
  // Generated code for module Transmit[T]
  // ########################################################################
  class Transmit[T, T0](
    ID1470769116: Place[T0, Multiset[T]], ID1470771631: Place[T0, Multiset[T]])(
      implicit ev0: Multiset[T] => Traversable[T0]) {
    val net = CPNGraph()
    // Places
    // ========================================================================
    // Generated code for transition Transmit and its arcs
    // ========================================================================
    case class BindingElement_ID1470773821(
      p: Option[T],
      success: Option[Boolean]) extends BindingElement
    val compatible_ID1470773821 = (b1: BindingElement_ID1470773821,
      b2: BindingElement_ID1470773821) =>
      (b1.p == b2.p || b1.p == None || b2.p == None) &&
        (b1.success == b2.success || b1.success == None || b2.success == None)
    val merge_ID1470773821 = (b1: BindingElement_ID1470773821,
      b2: BindingElement_ID1470773821) => {
      val p = if (b1.p == None) b2.p else b1.p
      val success = if (b1.success == None) b2.success else b1.success
      new BindingElement_ID1470773821(p, success)
    }
    val fullBindingElement_ID1470773821 = (be: BindingElement_ID1470773821) =>
      be.p != None &&
        be.success != None
    val guard_ID1470773821 = List()
    val enumBindings_ID1470773821 = List(
      List(BindingElement_ID1470773821(None, Some(true)),
        BindingElement_ID1470773821(None, Some(false))))
    val ID1470773821 = Transition("ID1470773821", "Transmit", 1,
      compatible_ID1470773821, merge_ID1470773821, fullBindingElement_ID1470773821,
      guard_ID1470773821, enumBindings_ID1470773821)
    net.addTransition(ID1470773821)
    // ------------------ Arcs ------------------
    // in ----> Transmit
    val pattern_ID1470775166_0 = Pattern((token: Any) => token match {
      case scala.Tuple2(_, (p: T)) =>
        BindingElement_ID1470773821(Some(p), None)
    }, 0)
    val eval_ID1470775166 = (be: BindingElement_ID1470773821) => (be.p) match {
      case (Some(p)) =>
        Multiset(scala.Tuple2(1, p))
    }
    val ID1470775166 = Arc("ID1470775166", ID1470769116, ID1470773821, In)(
      eval_ID1470775166)
    net.addArc(ID1470775166)
    // Transmit ----> out
    val eval_ID1470775627 = (be: BindingElement_ID1470773821) =>
      (be.success, be.p) match {
        case (Some(success), Some(p)) =>
          if (success)
            Multiset(scala.Tuple2(1, p))
          else
            Multiset[T]()
      }
    val ID1470775627 = Arc("ID1470775627", ID1470771631, ID1470773821, Out)(
      eval_ID1470775627)
    net.addArc(ID1470775627)
    // ------------------ /Arcs -----------------
    // Set ordered pattern binding basis for Transmit
    ID1470773821.orderedPatternBindingBasis = List(
      ArcPattern(ID1470775166, pattern_ID1470775166_0))
  }
  // ########################################################################
  // Generated code for module Sender
  // ########################################################################
  class Sender[T0, T1](ID1470848028: Place[T0, Multiset[Data]],
    ID1470848187: Place[T0, Multiset[Data]], ID1470848373: Place[T1, Multiset[Ack]])(
      implicit ev0: Multiset[Data] => Traversable[T0],
      ev1: Multiset[Ack] => Traversable[T1]) {
    val net = CPNGraph()
    // Places
    val ID1470848832 = Place("ID1470848832", "NextSend", Multiset(No(1)))
    net.addPlace(ID1470848832)
    // ========================================================================
    // Generated code for transition ReceiveAck and its arcs
    // ========================================================================
    case class BindingElement_ID1470848592(
      n: Option[Int],
      k: Option[Int]) extends BindingElement
    val compatible_ID1470848592 = (b1: BindingElement_ID1470848592,
      b2: BindingElement_ID1470848592) =>
      (b1.n == b2.n || b1.n == None || b2.n == None) &&
        (b1.k == b2.k || b1.k == None || b2.k == None)
    val merge_ID1470848592 = (b1: BindingElement_ID1470848592,
      b2: BindingElement_ID1470848592) => {
      val n = if (b1.n == None) b2.n else b1.n
      val k = if (b1.k == None) b2.k else b1.k
      new BindingElement_ID1470848592(n, k)
    }
    val fullBindingElement_ID1470848592 = (be: BindingElement_ID1470848592) =>
      be.n != None &&
        be.k != None
    val guard_ID1470848592 = List()
    val enumBindings_ID1470848592 = List()
    val ID1470848592 = Transition("ID1470848592", "ReceiveAck", 2,
      compatible_ID1470848592, merge_ID1470848592, fullBindingElement_ID1470848592,
      guard_ID1470848592, enumBindings_ID1470848592)
    net.addTransition(ID1470848592)
    // ------------------ Arcs ------------------
    // NextSend ----> ReceiveAck
    val pattern_ID1470851437_0 = Pattern((token: Any) => token match {
      case Tuple2(_, No((k: Int))) =>
        BindingElement_ID1470848592(None, Some(k))
    }, 1)
    val eval_ID1470851437 = (be: BindingElement_ID1470848592) => (be.k) match {
      case (Some(k)) =>
        No(k)
    }
    val ID1470851437 = Arc("ID1470851437", ID1470848832, ID1470848592, In)(
      eval_ID1470851437)
    net.addArc(ID1470851437)
    // d ----> ReceiveAck
    val pattern_ID1470852878_0 = Pattern((token: Any) => token match {
      case Tuple2(_, Ack((n: Int))) =>
        BindingElement_ID1470848592(Some(n), None)
    }, 0)
    val eval_ID1470852878 = (be: BindingElement_ID1470848592) => (be.n) match {
      case (Some(n)) =>
        Ack(n)
    }
    val ID1470852878 = Arc("ID1470852878", ID1470848373, ID1470848592, In)(
      eval_ID1470852878)
    net.addArc(ID1470852878)
    // ReceiveAck ----> NextSend
    val eval_ID1470852049 = (be: BindingElement_ID1470848592) => (be.n) match {
      case (Some(n)) =>
        No(n)
    }
    val ID1470852049 = Arc("ID1470852049", ID1470848832, ID1470848592, Out)(
      eval_ID1470852049)
    net.addArc(ID1470852049)
    // ------------------ /Arcs -----------------
    // Set ordered pattern binding basis for ReceiveAck
    ID1470848592.orderedPatternBindingBasis = List(
      ArcPattern(ID1470852878, pattern_ID1470852878_0),
      ArcPattern(ID1470851437, pattern_ID1470851437_0))
    // ========================================================================
    // Generated code for transition Send Packet and its arcs
    // ========================================================================
    case class BindingElement_ID1470849105(
      n: Option[Int],
      d: Option[String]) extends BindingElement
    val compatible_ID1470849105 = (b1: BindingElement_ID1470849105,
      b2: BindingElement_ID1470849105) =>
      (b1.n == b2.n || b1.n == None || b2.n == None) &&
        (b1.d == b2.d || b1.d == None || b2.d == None)
    val merge_ID1470849105 = (b1: BindingElement_ID1470849105,
      b2: BindingElement_ID1470849105) => {
      val n = if (b1.n == None) b2.n else b1.n
      val d = if (b1.d == None) b2.d else b1.d
      new BindingElement_ID1470849105(n, d)
    }
    val fullBindingElement_ID1470849105 = (be: BindingElement_ID1470849105) =>
      be.n != None &&
        be.d != None
    val guard_ID1470849105 = List()
    val enumBindings_ID1470849105 = List()
    val ID1470849105 = Transition("ID1470849105", "Send Packet", 1,
      compatible_ID1470849105, merge_ID1470849105, fullBindingElement_ID1470849105,
      guard_ID1470849105, enumBindings_ID1470849105)
    net.addTransition(ID1470849105)
    // ------------------ Arcs ------------------
    // NextSend ----> Send Packet
    val eval_ID1470850012 = (be: BindingElement_ID1470849105) => (be.n) match {
      case (Some(n)) =>
        No(n).next
    }
    val ID1470850012 = Arc("ID1470850012", ID1470848832, ID1470849105, In)(
      eval_ID1470850012)
    net.addArc(ID1470850012)
    // packets to send ----> Send Packet
    val pattern_ID1470849388IN_0 = Pattern((token: Any) => token match {
      case Tuple2(_, Data((n: Int), (d: String))) =>
        BindingElement_ID1470849105(Some(n), Some(d))
    }, 0)
    val eval_ID1470849388IN = (be: BindingElement_ID1470849105) =>
      (be.n, be.d) match {
        case (Some(n), Some(d)) =>
          Data(n, d)
      }
    val ID1470849388IN = Arc("ID1470849388IN", ID1470848028, ID1470849105, In)(
      eval_ID1470849388IN)
    net.addArc(ID1470849388IN)
    // Send Packet ----> NextSend
    val eval_ID1470850492 = (be: BindingElement_ID1470849105) => (be.n) match {
      case (Some(n)) =>
        No(n)
    }
    val ID1470850492 = Arc("ID1470850492", ID1470848832, ID1470849105, Out)(
      eval_ID1470850492)
    net.addArc(ID1470850492)
    // Send Packet ----> packets to send
    val eval_ID1470849388OUT = (be: BindingElement_ID1470849105) =>
      (be.n, be.d) match {
        case (Some(n), Some(d)) =>
          Data(n, d)
      }
    val ID1470849388OUT = Arc("ID1470849388OUT", ID1470848028, ID1470849105, Out)(
      eval_ID1470849388OUT)
    net.addArc(ID1470849388OUT)
    // Send Packet ----> a
    val eval_ID1470853969 = (be: BindingElement_ID1470849105) =>
      (be.n, be.d) match {
        case (Some(n), Some(d)) =>
          Data(n, d)
      }
    val ID1470853969 = Arc("ID1470853969", ID1470848187, ID1470849105, Out)(
      eval_ID1470853969)
    net.addArc(ID1470853969)
    // ------------------ /Arcs -----------------
    // Set ordered pattern binding basis for Send Packet
    ID1470849105.orderedPatternBindingBasis = List(
      ArcPattern(ID1470849388IN, pattern_ID1470849388IN_0))
    // Wire modules
  }
  // ########################################################################
  // Generated code for module Receiver
  // ########################################################################
  class Receiver[T0, T1, T2](
    ID1470723480: Place[T0, Multiset[Data]],
    ID1470725592: Place[T1, Multiset[Ack]],
    ID1470734702: Place[T2, Multiset[String]])(
      implicit ev0: Multiset[Data] => Traversable[T0],
      ev1: Multiset[Ack] => Traversable[T1],
      ev2: Multiset[String] => Traversable[T2]) {
    val net = CPNGraph()
    // Places
    val ID1470723819 = Place("ID1470723819", "NextRec", Multiset(1))
    net.addPlace(ID1470723819)
    // ========================================================================
    // Generated code for transition Discard Packet and its arcs
    // ========================================================================
    case class BindingElement_ID1470724324(
      n: Option[Int],
      d: Option[String],
      k: Option[Int]) extends BindingElement
    val compatible_ID1470724324 = (b1: BindingElement_ID1470724324,
      b2: BindingElement_ID1470724324) =>
      (b1.n == b2.n || b1.n == None || b2.n == None) &&
        (b1.d == b2.d || b1.d == None || b2.d == None) &&
        (b1.k == b2.k || b1.k == None || b2.k == None)
    val merge_ID1470724324 = (b1: BindingElement_ID1470724324,
      b2: BindingElement_ID1470724324) => {
      val n = if (b1.n == None) b2.n else b1.n
      val d = if (b1.d == None) b2.d else b1.d
      val k = if (b1.k == None) b2.k else b1.k
      new BindingElement_ID1470724324(n, d, k)
    }
    val fullBindingElement_ID1470724324 = (be: BindingElement_ID1470724324) =>
      be.n != None &&
        be.d != None &&
        be.k != None
    val guard_ID1470724324_0 = EvalGuard((be: BindingElement_ID1470724324) =>
      (be.n, be.k) match {
        case (Some(n), Some(k)) => {
          n.$bang$eq(k)
        }
      }, 0)
    val guard_ID1470724324 = List(guard_ID1470724324_0)
    val enumBindings_ID1470724324 = List()
    val ID1470724324 = Transition("ID1470724324", "Discard Packet", 2,
      compatible_ID1470724324, merge_ID1470724324, fullBindingElement_ID1470724324,
      guard_ID1470724324, enumBindings_ID1470724324)
    net.addTransition(ID1470724324)
    // ------------------ Arcs ------------------
    // NextRec ----> Discard Packet
    val pattern_ID1470727987IN_0 = Pattern((token: Any) => token match {
      case Tuple2(_, (k: Int)) =>
        BindingElement_ID1470724324(None, None, Some(k))
    }, 1)
    val eval_ID1470727987IN = (be: BindingElement_ID1470724324) => (be.k) match {
      case (Some(k)) =>
        k
    }
    val ID1470727987IN = Arc("ID1470727987IN", ID1470723819, ID1470724324, In)(
      eval_ID1470727987IN)
    net.addArc(ID1470727987IN)
    // b ----> Discard Packet
    val pattern_ID1470726221_0 = Pattern((token: Any) => token match {
      case Tuple2(_, Data((n: Int), (d: String))) =>
        BindingElement_ID1470724324(Some(n), Some(d), None)
    }, 0)
    val eval_ID1470726221 = (be: BindingElement_ID1470724324) =>
      (be.n, be.d) match {
        case (Some(n), Some(d)) =>
          Data(n, d)
      }
    val ID1470726221 = Arc("ID1470726221", ID1470723480, ID1470724324, In)(
      eval_ID1470726221)
    net.addArc(ID1470726221)
    // Discard Packet ----> NextRec
    val eval_ID1470727987OUT = (be: BindingElement_ID1470724324) =>
      (be.k) match {
        case (Some(k)) =>
          k
      }
    val ID1470727987OUT = Arc("ID1470727987OUT", ID1470723819, ID1470724324, Out)(
      eval_ID1470727987OUT)
    net.addArc(ID1470727987OUT)
    // Discard Packet ----> c
    val eval_ID1470732359 = (be: BindingElement_ID1470724324) => (be.k) match {
      case (Some(k)) =>
        Ack(k)
    }
    val ID1470732359 = Arc("ID1470732359", ID1470725592, ID1470724324, Out)(
      eval_ID1470732359)
    net.addArc(ID1470732359)
    // ------------------ /Arcs -----------------
    // Set ordered pattern binding basis for Discard Packet
    ID1470724324.orderedPatternBindingBasis = List(
      ArcPattern(ID1470726221, pattern_ID1470726221_0),
      ArcPattern(ID1470727987IN, pattern_ID1470727987IN_0))
    // ========================================================================
    // Generated code for transition Receive Packet and its arcs
    // ========================================================================
    case class BindingElement_ID1470725136(
      d: Option[String],
      k: Option[Int],
      a: Option[Int],
      data: Option[String]) extends BindingElement
    val compatible_ID1470725136 = (b1: BindingElement_ID1470725136,
      b2: BindingElement_ID1470725136) =>
      (b1.d == b2.d || b1.d == None || b2.d == None) &&
        (b1.k == b2.k || b1.k == None || b2.k == None) &&
        (b1.a == b2.a || b1.a == None || b2.a == None) &&
        (b1.data == b2.data || b1.data == None || b2.data == None)
    val merge_ID1470725136 = (b1: BindingElement_ID1470725136,
      b2: BindingElement_ID1470725136) => {
      val d = if (b1.d == None) b2.d else b1.d
      val k = if (b1.k == None) b2.k else b1.k
      val a = if (b1.a == None) b2.a else b1.a
      val data = if (b1.data == None) b2.data else b1.data
      new BindingElement_ID1470725136(d, k, a, data)
    }
    val fullBindingElement_ID1470725136 = (be: BindingElement_ID1470725136) =>
      be.d != None &&
        be.k != None &&
        be.a != None &&
        be.data != None
    val guard_ID1470725136_0 = BindGuard((be: BindingElement_ID1470725136) =>
      (be.k) match {
        case (Some(k)) =>
          val a = k.$plus(1)
          BindingElement_ID1470725136(be.d, be.k, Some(a), be.data)
      }, 1)
    val guard_ID1470725136 = List(guard_ID1470725136_0)
    val enumBindings_ID1470725136 = List()
    val ID1470725136 = Transition("ID1470725136", "Receive Packet", 2,
      compatible_ID1470725136, merge_ID1470725136, fullBindingElement_ID1470725136,
      guard_ID1470725136, enumBindings_ID1470725136)
    net.addTransition(ID1470725136)
    // ------------------ Arcs ------------------
    // NextRec ----> Receive Packet
    val pattern_ID1470730054_0 = Pattern((token: Any) => token match {
      case Tuple2(_, (k: Int)) =>
        BindingElement_ID1470725136(None, Some(k), None, None)
    }, 1)
    val eval_ID1470730054 = (be: BindingElement_ID1470725136) => (be.k) match {
      case (Some(k)) =>
        k
    }
    val ID1470730054 = Arc("ID1470730054", ID1470723819, ID1470725136, In)(
      eval_ID1470730054)
    net.addArc(ID1470730054)
    // b ----> Receive Packet
    val pattern_ID1470726893_0 = Pattern((token: Any) => token match {
      case Tuple2(_, Data((k: Int), (d: String))) =>
        BindingElement_ID1470725136(Some(d), Some(k), None, None)
    }, 1)
    val eval_ID1470726893 = (be: BindingElement_ID1470725136) =>
      (be.k, be.d) match {
        case (Some(k), Some(d)) =>
          Data(k, d)
      }
    val ID1470726893 = Arc("ID1470726893", ID1470723480, ID1470725136, In)(
      eval_ID1470726893)
    net.addArc(ID1470726893)
    // received ----> Receive Packet
    val pattern_ID1470737284_0 = Pattern((token: Any) => token match {
      case Tuple2(_, (data: String)) =>
        BindingElement_ID1470725136(None, None, None, Some(data))
    }, 0)
    val eval_ID1470737284 = (be: BindingElement_ID1470725136) =>
      (be.data) match {
        case (Some(data)) =>
          data
      }
    val ID1470737284 = Arc("ID1470737284", ID1470734702, ID1470725136, In)(
      eval_ID1470737284)
    net.addArc(ID1470737284)
    // Receive Packet ----> NextRec
    val eval_ID1470730942 = (be: BindingElement_ID1470725136) =>
      (be.a) match {
        case (Some(a)) =>
          a
      }
    val ID1470730942 = Arc("ID1470730942", ID1470723819, ID1470725136, Out)(
      eval_ID1470730942)
    net.addArc(ID1470730942)
    // Receive Packet ----> c
    val eval_ID1470733367 = (be: BindingElement_ID1470725136) =>
      (be.a) match {
        case (Some(a)) =>
          Ack(a)
      }
    val ID1470733367 = Arc("ID1470733367", ID1470725592, ID1470725136, Out)(
      eval_ID1470733367)
    net.addArc(ID1470733367)
    // Receive Packet ----> received
    val eval_ID1470735543 = (be: BindingElement_ID1470725136) =>
      (be.data, be.d) match {
        case (Some(data), Some(d)) =>
          data.$plus(d)
      }
    val ID1470735543 = Arc("ID1470735543", ID1470734702, ID1470725136, Out)(
      eval_ID1470735543)
    net.addArc(ID1470735543)
    // ------------------ /Arcs -----------------
    // Set ordered pattern binding basis for Receive Packet
    ID1470725136.orderedPatternBindingBasis = List(
      ArcPattern(ID1470737284, pattern_ID1470737284_0),
      ArcPattern(ID1470726893, pattern_ID1470726893_0),
      guard_ID1470725136_0)
    // Wire modules
  }
  // ########################################################################
  // Generated code for module Network[A,B]
  // ########################################################################
  class Network[A, B, T0, T1](ID1470791644: Place[T0, Multiset[A]],
    ID1470793324: Place[T0, Multiset[A]], ID1470795031: Place[T1, Multiset[B]],
    ID1470796765: Place[T1, Multiset[B]])(
      implicit ev0: Multiset[A] => Traversable[T0],
      ev1: Multiset[B] => Traversable[T1]) {
    val net = CPNGraph()
    // Places
    // Wire modules
    val st0 = new Transmit(ID1470791644, ID1470793324)
    net.addSubstitutionTransition(st0.net)
    val st1 = new Transmit(ID1470796765, ID1470795031)
    net.addSubstitutionTransition(st1.net)
  }
  Simulator.run(net)
}

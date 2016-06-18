package mvp.volvo.tlv.tlv

case class TlvException(message: String, data: Array[Byte], cause: Throwable) extends RuntimeException(message, cause)
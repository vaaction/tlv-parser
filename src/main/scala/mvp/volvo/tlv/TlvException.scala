package mvp.volvo.tlv

class TlvException(message: String, data: Array[Byte], cause: Throwable)
  extends RuntimeException(message, cause) {

  def this(message: String, data: Array[Byte]) {
    this(message, data, null)
  }
}

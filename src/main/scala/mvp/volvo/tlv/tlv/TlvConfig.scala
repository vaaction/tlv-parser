package mvp.volvo.tlv.tlv

import java.nio.ByteOrder

case class TlvConfig(size: Int, order: ByteOrder)

object TlvConfig {
  def apply(): TlvConfig = new TlvConfig(2, ByteOrder.BIG_ENDIAN)
}
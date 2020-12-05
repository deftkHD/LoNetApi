package de.deftk.lonet.api.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class BooleanFromIntSerializer: KSerializer<Boolean> {
    override fun deserialize(decoder: Decoder) = decoder.decodeInt() != 0
    override fun serialize(encoder: Encoder, value: Boolean) = encoder.encodeInt(if (value) 1 else 0)
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Boolean", PrimitiveKind.BOOLEAN)
}
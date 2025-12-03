package danilfedorov.shared_shoppinglist.util

import danilfedorov.domain_core.model.Product
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.net.URLDecoder
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object ShoppingListCompressor {

    fun zip(name: String, products: List<Product>): String = ByteArrayOutputStream().use { baos ->
        GZIPOutputStream(baos).use { gzip ->
            java.io.DataOutputStream(gzip).use { dos ->
                dos.writeUTF(name)

                dos.writeInt(products.size)

                products.forEach { p ->
                    dos.writeUTF(p.name)
                    dos.writeBoolean(p.isSelected)
                    dos.writeUTF(p.amount)
                    dos.writeUTF(p.unit)
                }
            }
        }
        Base64.getUrlEncoder().withoutPadding().encodeToString(baos.toByteArray())
            .replace('+', '-').replace('/', '_')
    }

    fun unzip(data: String): Pair<String, List<Product>> {
        val fixed = URLDecoder.decode(data, "UTF-8")

        val bytes = fixed
            .replace('-', '+')
            .replace('_', '/')
            .let { Base64.getDecoder().decode(it) }

        return GZIPInputStream(bytes.inputStream()).use { gzip ->
            DataInputStream(gzip).use { dis ->
                val listName = dis.readUTF()

                val count = dis.readInt()
                val products = ArrayList<Product>(count)

                repeat(count) {
                    products += Product(
                        listId = 0,
                        name = dis.readUTF(),
                        isSelected = dis.readBoolean(),
                        amount = dis.readUTF(),
                        unit = dis.readUTF()
                    )
                }

                listName to products
            }
        }
    }
}
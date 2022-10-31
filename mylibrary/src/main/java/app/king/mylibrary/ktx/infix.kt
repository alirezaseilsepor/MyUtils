package app.king.mylibrary.ktx


infix fun <A, B, C> Pair<A, B>.toTriple(that: C): Triple<A, B, C> = Triple(this.first, this.second, that)
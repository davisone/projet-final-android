package fr.sdv.b3dev.evan.projet_final.data

import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.SneakerEntity
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.UserEntity

object SampleData {

    val defaultUser = UserEntity(
        id = 1,
        email = "user@snkrs.com",
        displayName = "Sneaker Fan"
    )

    private val currentTime = System.currentTimeMillis()
    private val oneDay = 24 * 60 * 60 * 1000L
    private val oneWeek = 7 * oneDay

    val sampleSneakers = listOf(
        // Nike
        SneakerEntity(
            id = 1,
            name = "Air Jordan 1 Retro High OG",
            brand = "Nike",
            description = "L'Air Jordan 1 Retro High OG revient dans un coloris classique. Cette sneaker iconique présente une tige en cuir premium avec le swoosh emblématique.",
            price = 180.0,
            imageUrl = "https://picsum.photos/seed/snkrs-aj1-retro/1200/900",
            releaseDate = currentTime - 5 * oneDay,
            isUpcoming = false,
            colorway = "Chicago",
            sizes = "38,39,40,41,42,43,44,45,46",
            barcode = "194956789012"
        ),
        SneakerEntity(
            id = 2,
            name = "Nike Dunk Low",
            brand = "Nike",
            description = "Un classique du basketball devenu icône streetwear. Le Nike Dunk Low offre un style intemporel avec son design épuré.",
            price = 119.0,
            imageUrl = "https://picsum.photos/seed/snkrs-dunk-low/1200/900",
            releaseDate = currentTime - 2 * oneDay,
            isUpcoming = false,
            colorway = "Panda",
            sizes = "36,37,38,39,40,41,42,43,44,45",
            barcode = "194956789013"
        ),
        SneakerEntity(
            id = 3,
            name = "Air Force 1 '07",
            brand = "Nike",
            description = "La légende continue. L'Air Force 1 '07 apporte un look frais avec son cuir premium et son amorti Air légendaire.",
            price = 129.0,
            imageUrl = "https://picsum.photos/seed/snkrs-af1/1200/900",
            releaseDate = currentTime - 10 * oneDay,
            isUpcoming = false,
            colorway = "Triple White",
            sizes = "38,39,40,41,42,43,44,45,46,47",
            barcode = "194956789014"
        ),
        SneakerEntity(
            id = 4,
            name = "Air Max 90",
            brand = "Nike",
            description = "Née en 1990, la Air Max 90 reste une référence du style. Son design audacieux et sa bulle Air visible définissent le look streetwear.",
            price = 149.0,
            imageUrl = "https://picsum.photos/seed/snkrs-am90/1200/900",
            releaseDate = currentTime - 3 * oneDay,
            isUpcoming = false,
            colorway = "Infrared",
            sizes = "39,40,41,42,43,44,45",
            barcode = "194956789015"
        ),

        // Adidas
        SneakerEntity(
            id = 5,
            name = "Yeezy Boost 350 V2",
            brand = "Adidas",
            description = "La Yeezy Boost 350 V2 combine confort et style avec sa semelle Boost et sa tige Primeknit signature.",
            price = 230.0,
            imageUrl = "https://picsum.photos/seed/snkrs-yeezy-350/1200/900",
            releaseDate = currentTime + 3 * oneDay,
            isUpcoming = true,
            colorway = "Bone",
            sizes = "38,39,40,41,42,43,44,45,46",
            barcode = "194956789016"
        ),
        SneakerEntity(
            id = 6,
            name = "Forum Low",
            brand = "Adidas",
            description = "Inspirée du basketball des années 80, la Forum Low revient avec son design rétro et son confort moderne.",
            price = 110.0,
            imageUrl = "https://picsum.photos/seed/snkrs-forum-low/1200/900",
            releaseDate = currentTime - 7 * oneDay,
            isUpcoming = false,
            colorway = "Cloud White",
            sizes = "38,39,40,41,42,43,44,45",
            barcode = "194956789017"
        ),
        SneakerEntity(
            id = 7,
            name = "Samba OG",
            brand = "Adidas",
            description = "Un classique intemporel. La Samba OG conserve son look authentique depuis des décennies.",
            price = 100.0,
            imageUrl = "https://picsum.photos/seed/snkrs-samba-og/1200/900",
            releaseDate = currentTime - 14 * oneDay,
            isUpcoming = false,
            colorway = "White/Black",
            sizes = "36,37,38,39,40,41,42,43,44,45,46",
            barcode = "194956789018"
        ),

        // New Balance
        SneakerEntity(
            id = 8,
            name = "New Balance 550",
            brand = "New Balance",
            description = "La 550 fait son grand retour. Ce modèle basketball des années 80 s'impose comme un incontournable du style casual.",
            price = 130.0,
            imageUrl = "https://picsum.photos/seed/snkrs-nb-550/1200/900",
            releaseDate = currentTime - 1 * oneDay,
            isUpcoming = false,
            colorway = "White/Green",
            sizes = "38,39,40,41,42,43,44,45",
            barcode = "194956789019"
        ),
        SneakerEntity(
            id = 9,
            name = "New Balance 2002R",
            brand = "New Balance",
            description = "Inspirée des archives running, la 2002R offre un confort exceptionnel grâce à sa semelle N-ERGY.",
            price = 160.0,
            imageUrl = "https://picsum.photos/seed/snkrs-nb-2002r/1200/900",
            releaseDate = currentTime + 5 * oneDay,
            isUpcoming = true,
            colorway = "Protection Pack",
            sizes = "40,41,42,43,44,45,46",
            barcode = "194956789020"
        ),

        // Jordan
        SneakerEntity(
            id = 10,
            name = "Air Jordan 4 Retro",
            brand = "Jordan",
            description = "La Air Jordan 4 revient dans un coloris très attendu. Mesh respirant et design iconique au rendez-vous.",
            price = 210.0,
            imageUrl = "https://picsum.photos/seed/snkrs-aj4-retro/1200/900",
            releaseDate = currentTime + oneWeek,
            isUpcoming = true,
            colorway = "Military Black",
            sizes = "38,39,40,41,42,43,44,45,46",
            barcode = "194956789021"
        ),
        SneakerEntity(
            id = 11,
            name = "Air Jordan 11 Retro",
            brand = "Jordan",
            description = "Élégance et performance. La Air Jordan 11 avec son cuir verni distinctif reste une des plus désirées.",
            price = 225.0,
            imageUrl = "https://picsum.photos/seed/snkrs-aj11-retro/1200/900",
            releaseDate = currentTime + 2 * oneWeek,
            isUpcoming = true,
            colorway = "Cool Grey",
            sizes = "40,41,42,43,44,45,46,47",
            barcode = "194956789022"
        ),

        // Puma
        SneakerEntity(
            id = 12,
            name = "Puma Suede Classic XXI",
            brand = "Puma",
            description = "Un classique depuis 1968. La Suede Classic continue d'incarner le style street culture.",
            price = 85.0,
            imageUrl = "https://picsum.photos/seed/snkrs-puma-suede/1200/900",
            releaseDate = currentTime - 20 * oneDay,
            isUpcoming = false,
            colorway = "Black/White",
            sizes = "38,39,40,41,42,43,44,45",
            barcode = "194956789023"
        ),

        // Converse
        SneakerEntity(
            id = 13,
            name = "Chuck Taylor All Star",
            brand = "Converse",
            description = "L'originale. La Chuck Taylor All Star reste la sneaker la plus iconique depuis 1917.",
            price = 75.0,
            imageUrl = "https://picsum.photos/seed/snkrs-converse-chuck/1200/900",
            releaseDate = currentTime - 30 * oneDay,
            isUpcoming = false,
            colorway = "Black",
            sizes = "35,36,37,38,39,40,41,42,43,44,45,46",
            barcode = "194956789024"
        ),

        // Reebok
        SneakerEntity(
            id = 14,
            name = "Reebok Club C 85",
            brand = "Reebok",
            description = "Née sur les courts de tennis, la Club C 85 est devenue une icône du style minimaliste.",
            price = 90.0,
            imageUrl = "https://picsum.photos/seed/snkrs-reebok-clubc/1200/900",
            releaseDate = currentTime - 15 * oneDay,
            isUpcoming = false,
            colorway = "Vintage White",
            sizes = "38,39,40,41,42,43,44,45",
            barcode = "194956789025"
        ),

        // Upcoming releases
        SneakerEntity(
            id = 15,
            name = "Travis Scott x Air Jordan 1 Low",
            brand = "Jordan",
            description = "La collaboration tant attendue avec Travis Scott. Design inversé et détails uniques.",
            price = 150.0,
            imageUrl = "https://picsum.photos/seed/snkrs-aj1-travis/1200/900",
            releaseDate = currentTime + 10 * oneDay,
            isUpcoming = true,
            colorway = "Reverse Mocha",
            sizes = "38,39,40,41,42,43,44,45,46",
            barcode = "194956789026"
        )
    )
}

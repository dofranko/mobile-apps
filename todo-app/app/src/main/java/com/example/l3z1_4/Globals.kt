package com.example.l3z1_4

class  Globals {
        companion object StaticObjects{
            val categoryBinding : LinkedHashMap<String, Int> =
                    linkedMapOf("Ogólne" to R.drawable.general_icon, "Sport" to R.drawable.sport_icon,
                            "Nauka" to R.drawable.science_icon, "Zakupy" to R.drawable.shopping_icon,
                            "Biznes" to R.drawable.business_icon)
            val categoryIcons : Array<Int> = arrayOf(R.drawable.general_icon, R.drawable.sport_icon,
                    R.drawable.science_icon, R.drawable.shopping_icon, R.drawable.business_icon)

            val importanceBinding : LinkedHashMap<String, Int> =
                    linkedMapOf("Wysoki" to R.drawable.importance_background_red)

        }
}
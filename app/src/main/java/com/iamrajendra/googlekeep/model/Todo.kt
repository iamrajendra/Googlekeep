package com.iamrajendra.googlekeep.model

import com.google.firebase.firestore.QueryDocumentSnapshot

class Todo : Model {
    var uid: String? = null
    var addedBy: String? = null
    var photo: String? = null
    var autherPhoto: String? = null

    var description: String? = null
    var  key:String ? = null
    var color:Int ? =0;

    constructor(title: QueryDocumentSnapshot) : super(title.get("title")!!.toString()) {
        description = title.get("description")?.toString()
        photo = title.get("photo")?.toString()
        addedBy = title.get("addedBy")?.toString()
        autherPhoto = title.get("autherPhoto")?.toString()
        uid = title.get("uid")?.toString()
        if (title?.get("color")!=null) {
            var lo: Long = title?.get("color") as Long;
            color = lo?.toInt()
        }
        key= title.id


    }

    constructor(title: String) : super(title) {

    }
    constructor()  {

    }
}

package code.vineet.colorpicker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    private lateinit var webView: WebView

    private lateinit var database: DatabaseReference
    private lateinit var sensref: DatabaseReference
    private var myRef = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {




        Incommingcalls()



        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("/")
         sensref = FirebaseDatabase.getInstance().reference
        setContentView(R.layout.activity_main)



        webView = findViewById(R.id.webview)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }


        colpic.isDrawingCacheEnabled = true
        colpic.buildDrawingCache(true)

        colpic.setOnTouchListener { v,event ->

            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE ) {
                bitmap = colpic.drawingCache

                var x =event.x.toInt()
                var y = event.y.toInt()

                if(x>=0 && y>0 && x< bitmap.width && y<bitmap.height) {

                    //tvweb.text = ""


                    val pixel = bitmap.getPixel(x, y)

                    var r = Color.red(pixel)
                    var g = Color.green(pixel)
                    var b = Color.blue(pixel)

                    tvRGB.text = "R: $r  G: $g B: $b"

                    var str: String = "$r"



                    res.setBackgroundColor(Color.rgb(r, g, b))

                    var m = hashMapOf(
                        "r" to r.toString(),
                        "g" to g.toString(),
                        "b" to b.toString()
                    )




                    database.setValue(m)
                }else{
                    //tvweb.text = "Crossed layout boundary"
                }






            }

            true

        }


        btImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }



        }





    fun Incommingcalls(){
        myRef.child("sensor")
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {



                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    try {
                        val td = dataSnapshot!!.value

                        Log.d("tagggg","$td")



                        if (td!=null){

                            var value:String

                            value = td.toString()
                           // sens.text = value

                        }
                    }catch (ex:Exception){}

                }



            })









    }


    var imageSelected: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            imageSelected = data.data
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageSelected)

            colpic.setImageBitmap(bitmap)
            bitmap = colpic.drawingCache



//            val bitmapDrawable = BitmapDrawable(bitmap)
//
//            btImage.setBackgroundDrawable(bitmapDrawable)

        }
    }
}




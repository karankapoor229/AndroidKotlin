package me.karankapoor.androidkotlin

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.util.Log

import com.github.tamir7.contacts.Contacts
import com.github.tamir7.contacts.Contact
import kotlinx.android.synthetic.main.activity_contacts.*
import pub.devrel.easypermissions.EasyPermissions
import com.opencsv.CSVWriter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_contacts.view.profile_image
import java.io.*
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe;
import org.reactivestreams.Subscriber


private const val READ_WRITE_CONTACT_PERM = 124


private val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE)

class ContactsActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {


    private val TAG = ContactsActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        Contacts.initialize(this)
        profile_image.setOnClickListener {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_contacts_rationale),
                    READ_WRITE_CONTACT_PERM, *perms)
        }
    }

    fun doTask(){
        Observable.create(ObservableOnSubscribe<Unit>
        { emitter -> emitter.onNext(getContacts()) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ result ->  handleResponse() }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    private fun handleResponse() {
        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()

    }

    private fun handleError(error: Throwable) {

        Log.d(TAG, error.localizedMessage)

        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
    }




    fun getContacts() {

        Log.d("here", Thread.currentThread().name)
        val contacts: List<Contact> = Contacts.getQuery().hasPhoneNumber().find()
        val csv = android.os.Environment.getExternalStorageDirectory().absolutePath
        val csv_writer = CSVWriter(FileWriter(csv + "/temp.csv"), ',')
        val data = ArrayList<Array<String>>()
        try {
            for (item in contacts) {
//                data.add(arrayOf(item.displayName, item.givenName, item.companyName, item.companyTitle, item.familyName, item.note, item.photoUri, item.addresses.joinToString("|"), item.anniversary.toString(), item.emails.joinToString("|"), item.phoneNumbers.joinToString("|")))
                var phoneNumbers = ""
                for (phone in item.phoneNumbers) {
                    phoneNumbers = phoneNumbers + "|" + phone.number
                }
                data.add(arrayOf(item.displayName, phoneNumbers))
            }
        } catch (e: Exception) {
            Log.d("catch", e.toString())
        }

        csv_writer.writeAll(data)
        csv_writer.close()

        try {
            zipAFile("$csv/temp.csv","$csv/csv.zip")

        } catch (e: Exception) {
            throw e
        }
        val file = File("$csv/temp.csv")
        file.delete()

    }


    override fun onPermissionsGranted(requestCode: Int, list: List<String>) {
        // Some permissions have been granted
        doTask()
    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
        // Some permissions have been denied
        // ...
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
    }
    fun zipAFile(sourceFile: String, destFile:String){
        Log.d("dest", destFile)
        val fos = FileOutputStream(destFile)
        val zipOut = ZipOutputStream(fos)
        val fileToZip = File(sourceFile)
        val fis = FileInputStream(fileToZip)
        val zipEntry = ZipEntry(fileToZip.name)
        zipOut.putNextEntry(zipEntry)
        val bytes = ByteArray(1024)
        var length: Int = 0
        while ({length = fis.read(bytes);length}() >= 0) {
            zipOut.write(bytes, 0, length)
        }
        zipOut.close()
        fis.close()
        fos.close()
    }
}
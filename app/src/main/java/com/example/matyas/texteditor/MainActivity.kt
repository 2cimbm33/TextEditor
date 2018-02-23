package com.example.matyas.texteditor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0
const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu) = menuInflater?.let {
        it.inflate(R.menu.menu, menu)
        true
    } ?: false

    override fun onOptionsItemSelected(item: MenuItem?) = item?.let {
        when (it.itemId) {
            R.id.save -> {
                val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

                when (permission) {
                    PackageManager.PERMISSION_GRANTED -> {
                        saveFile()
                    }

                    PackageManager.PERMISSION_DENIED -> {
                        ActivityCompat.requestPermissions(this,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                        false
                    }

                    else -> false
                }

            }

            R.id.load -> {
                val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

                when (permission) {
                    PackageManager.PERMISSION_GRANTED -> {
                        invokeLoadFiles()
                    }

                    PackageManager.PERMISSION_DENIED -> {
                        ActivityCompat.requestPermissions(this,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                        false
                    }

                    else -> false
                }
            }

            R.id.preferences -> {
                throw NotImplementedError("Preferences activity isn't implemented.")
            }

            else -> false
        }
    } ?: false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) {
                val extras = data?.extras ?: Bundle()
                val name = extras.getString("fileName")

                val result = FileOperations.loadFile(name)

                if (result is LoadSuccess) {
                    fileName.text.clear()
                    fileName.text.append(name)
                    textEdit.text.clear()
                    textEdit.text.append(result.text)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveFile()
                }
            }
        }
    }

    private fun saveFile() : Boolean {
        val name = fileName.text.toString()
        val text = textEdit.text.toString()

        val result = FileOperations.saveFile(name, text)
        return result is WriteSuccess

        /*when (result) {
            is WriteSuccess -> {
                fileName.text.clear()
                textEdit.text.clear()
                true
            }

            else -> false
        }*/
    }

    private fun invokeLoadFiles() : Boolean {
        val intent = Intent(this, LoadFileListActivity::class.java)
        startActivityForResult(intent, 0)
        return true
    }
}

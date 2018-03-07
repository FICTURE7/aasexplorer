package com.ficture7.aasexplorer

import com.ficture7.aasexplorer.client.XtremePapersClient
import com.ficture7.aasexplorer.model.examination.ALevelExamination
import com.ficture7.aasexplorer.store.CsvStore
import org.junit.Ignore
import org.junit.Test

class Main {

    @Ignore
    @Test
    fun sandbox() {
        val explorer = ExplorerBuilder()
                .useStore(CsvStore::class.java) {
                    instance ->  instance.configure("data")
                }
                .withClient(XtremePapersClient::class.java)
                .withExamination(ALevelExamination::class.java)
                .build()

        System.out.println("Loading async...")
        explorer.aLevel.subjects.loadAsync {
            onLoad {
                System.out.println("Loaded")

                val physics = explorer.aLevel.subjects[9702]

                if (physics != null) {
                    System.out.println(physics)
                    System.out.println("Loading resources of physics...")

                    physics.resources.loadAsync {
                        onLoad {
                            System.out.println("Loaded resources of physics!")
                            physics.resources.questionPapers.forEach {
                                System.out.println(it)
                            }
                        }
                    }
                } else {
                    System.out.println("Could not find Physics subject.")
                }

                System.out.println("Saving async...")
                explorer.aLevel.subjects.saveAsync {
                    onSave {
                        System.out.println("Saved!")
                    }
                    onError {
                        System.out.println(it)
                    }
                }
            }

            onError {
                System.out.println(it)
            }
        }

        System.out.println("Waiting for 10s.")
        Thread.sleep(10000)
    }
}
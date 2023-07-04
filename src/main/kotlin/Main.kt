import java.lang.RuntimeException

fun main(args: Array<String>) {
    println("Hello Kotlin!")
    val comm1 = Comment(1, 1, false, "Начальный коммент")
    val comm2 = Comment(2, 1, false, "Второй коммент для проверки изменений")
    val note1 = Note(1, "заголовок1", "Текст1")
    val note2 = Note(2, "заголовок2", "Текст2")

    println("Добавляем заметки 1,2")
    NoteService.add(note1)
    NoteService.add(note2)
    NoteService.get()

    println("Создаём комментарии к заметкам и показываем по id заметки")
    NoteService.createComment(1, Comment(1, 1,false, "1-1"))
    NoteService.createComment(1, Comment(1, 2,false, "1-2"))
    NoteService.createComment(1, Comment(1, 3, false,"1-3"))
    NoteService.createComment(2, Comment(2, 1,false, "2-1"))
    NoteService.createComment(2, Comment(2, 2,false, "2-2"))
    NoteService.getComments(2)

    println("Удаляем заметку 1")
    NoteService.deleteNote(1)
    NoteService.get()

    println("Редактируем заметку 2")
    NoteService.edit(Note(2,"New Title", "New Text"))
    NoteService.get()

    println("Показываем заметку по id 2")
    NoteService.getById(2)

    println("Удаляем комментарий по его id")
    NoteService.deleteComment(4)
    NoteService.getComments(2)

    println("Редактируем комментарий по его id")
    NoteService.editComment(5,"NewText")
    NoteService.getComments(2)

    println("Восстановим комментарий по его id")
    NoteService.restoreComments(4)
    NoteService.getComments(2)
}

data class Note(val id: Int, val title: String, val text: String)
data class Comment(val idNote: Int, val idCom: Int, var delCom: Boolean, val message: String)
class NoteNotFoundException(message: String) :
    RuntimeException(message) //throw NoteNotFoundException("Нет заметки с таким $id")

object NoteService {
    private var lastID: Int = 0
    private var lastIDComm: Int = 0
    private val listNote = mutableListOf<Note>()
    private val listComment = mutableListOf<Comment>()

    fun clear() {
        listNote.clear()
        listComment.clear()
        lastID = 0
        lastIDComm = 0
    }

    fun add(item: Note): Note {                 //Создает новую заметку у текущего пользователя/NoteService.add(note1)
        listNote.add(item.copy(id = ++lastID))
        return listNote.last()
    }

    fun deleteNote(idNote: Int): Boolean {         //Удаляет заметку текущего пользователя по id/NoteService.delete(1)
        for (item in listNote) {
                if (idNote == item.id) {
                    listNote.remove(item)
                    deleteComment(item.id)
                    return true
            }
        }
        throw NoteNotFoundException("Нет заметки с таким $idNote")
    }

    fun edit(item: Note): Boolean {         //Редактирует заметку текущего пользователя/NoteService.edit(note(1,"New Title", "NewText"))
        for (i in listNote) {
            if (item.id == i.id) {
                val index = listNote.indexOf(i)
                listNote[index] = item.copy()
                return true
            }
        }
        throw NoteNotFoundException("Нет такой заметки $item")
    }

    fun get() {                          //Возвращает список заметок созданных пользователем/NoteService.get()
        if (listNote.isNotEmpty()) {
            for (item in listNote) {
                print(item)
                println()
            }
        } else {
            throw NoteNotFoundException("Нет заметок в списке")
        }
    }

    fun getById(id: Int): Note {       //Возвращает заметку по её id/NoteService.getById(2)
        for (item in listNote) {
            if (item.id == id) {
                print(item)
                println()
                return item
            }
        }
        throw NoteNotFoundException("Нет заметки с таким id: $id")
    }

    fun createComment(
        noteId: Int,
        comment: Comment
    ): Comment {   //Добавляет новый комментарий к заметке/NoteService.createComment(2, Comment(1,"1"))
        for (note in listNote) {
            if (noteId == note.id) {
                listComment.add(comment.copy(idCom = ++lastIDComm))
                return listComment.last()
            }
        }
        throw NoteNotFoundException("Нет заметки с таким $noteId")
    }

    fun getComments(noteId: Int) {           //Возвращает список комм к заметке/NoteService.getComments(2, Comment(1,"1"))
        for (comm in listComment) {
            if (noteId == comm.idNote && !comm.delCom) {
                print(comm)
                println()
            }
        }
    }

    fun deleteComment(idCom: Int): Boolean {         //Удаляет комментарий к заметке/ NoteService.deleteComment(4)
        for (item in listComment) {
            if (idCom == item.idCom && !item.delCom) {
                item.delCom = true
                //listComment.remove(item)
                return true
            }
        }
        throw NoteNotFoundException("Нет комментария с таким $idCom")
    }

    fun editComment(
        idCom: Int,
        text: String
    ): Boolean {     //Редактирует указанный комм у заметки/NoteService.editComment(Comment(5,"NewText"))
        for (item in listComment) {
            if (idCom == item.idCom && !item.delCom) {
                listComment.remove(item)
                listComment.add(item.copy(message = text))
                return true
            }
        }
        throw NoteNotFoundException("Нет заметки с таким $idCom")
    }

    fun restoreComments(idCom: Int): Boolean {          //Восстанавливает удаленный комментарий/ NoteService.restoreComments(5)
        for (item in listComment) {
            if (idCom == item.idCom && item.delCom) {
                item.delCom = false
                return true
            }
        }
        throw NoteNotFoundException("Нет удалённого комментария с таким $idCom")
    }

}
/*    fun delete(item: Note): Boolean {           //Удаляет заметку текущего пользователя по имени заметки/NoteService.delete(note1)
        val index = listNote.indexOf(item)      //Подумать как удалять все комменты к этой заметке??
        if (index >= 0) {
            listNote.removeAt(index)
            return true
        }
        throw NoteNotFoundException("Нет такой заметки $item")
    }*/

/*
private val IDNoteComm: MutableMap<Int, Comment> = mutableMapOf()  //Попытка через связ.списки-фиаско
fun cC(nI: Int, com: Comment) {
        for (note in listNote) {
            if (nI == note.id) {
                IDNoteComm.put(nI, com.copy(idCom = ++lastIDComm))
                listComment.add(com.copy(idCom = ++lastIDComm))
            }
        }
        throw NoteNotFoundException("Нет заметки с таким $nI")
    }*/

/*fun gC(noteId: Int){
        for(item in IDNoteComm){
            if(item.key==noteId){
                print(item.value)
                println()
            }
        }
    }*/
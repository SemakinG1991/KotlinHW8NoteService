import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class NoteServiceTest {

    @Before
    fun clearBeforeTest() {
        NoteService.clear()
    }

    @Test
    fun addNote() {
        val service = NoteService
        val note = service.add(Note(1, "Title", "Text"))
        val result = note.id
        assertEquals(1, result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun delNote() {
        val service = NoteService
        val note1 = service.add(Note(1, "Title", "Text"))
        service.deleteNote(1)
        val result=note1.id

        assertEquals(1, result)
    }
    @Test
    fun editNote() {
        val service = NoteService
        val note1 = service.add(Note(1, "Title", "Text"))
        val result = service.edit(Note(1, "Tt","txt"))
        assertTrue(result)
    }
    @Test(expected = NoteNotFoundException::class)
    fun noEditNote() {
        val service = NoteService
        val note1 = service.add(Note(1, "Title", "Text"))
        val result = service.edit(Note(2, "Tt","txt"))
        assertTrue(result)
    }


}
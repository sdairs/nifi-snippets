import org.apache.nifi.serialization.record.MapRecord
import org.apache.nifi.serialization.record.RecordField
import org.apache.nifi.serialization.RecordReader
import org.apache.nifi.serialization.SimpleRecordSchema
import org.apache.nifi.controller.AbstractControllerService
import org.apache.nifi.logging.ComponentLog
import org.apache.nifi.serialization.MalformedRecordException
import org.apache.nifi.serialization.record.Record
import org.apache.nifi.serialization.RecordReaderFactory
import org.apache.nifi.serialization.record.RecordSchema
import org.apache.nifi.serialization.record.RecordFieldType

class KVPReader implements RecordReader {

    private final BufferedReader bufferedReader

    public KVPReader(InputStream input) {
        bufferedReader = new BufferedReader(new InputStreamReader(input))
    }

    public Record nextRecord(final boolean coerceTypes, final boolean dropUnknownFields) throws IOException, MalformedRecordException {
        final String line = bufferedReader.readLine()
        if (line == null) {
            return null
        }

        List<String> kvps = line.tokenize(' ')
        Map<String, Object> recordValues = [:]
        List<RecordField> recordFields = []
        kvps.each { row ->
            String key = ''
            String value = ''
            (key,value) = row.tokenize(':')
            recordValues.put(key, value)
            recordFields.add(new RecordField(key, RecordFieldType.STRING.getDataType()))
        }
        SimpleRecordSchema schema = new SimpleRecordSchema(recordFields)
        return new MapRecord(schema, recordValues)
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close()
    }

    @Override
    public RecordSchema getSchema() {
        return schema
    }

}

class KVPReaderFactory extends AbstractControllerService implements RecordReaderFactory {

    public KVPReaderFactory() {
    }

    public RecordReader createRecordReader(final Map<String, String> variables, final InputStream inputStream, final long inputLength, final ComponentLog componentLog) throws IOException {
        return new KVPReader(inputStream)
    }

}

reader = new KVPReaderFactory()

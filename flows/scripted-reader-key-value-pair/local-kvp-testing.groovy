test_data = '''name:dennis age:45 job:veterinarian
name:dee age:45 job:actress
name:mac age:44 job:sheriff
name:charlie age:45 job:lawyer'''

Map<String, Object> parse(String line) {
    if (line == null) {
        return null
    }

    List<String> kvps = line.tokenize(' ')
    Map<String, Object> recordValues = [:]
    kvps.each { row ->
            String key = ''
            String value = ''
            (key,value) = row.tokenize(':')
            recordValues.put(key, value)
    }
    return recordValues
}

records = []
test_data.split('\\n').each { line ->
    records.add(parse(line))
}
print(records)
# Elpis API

The Elpis API currently supports only single-session use - i.e. using multiple datasets or
transcribing multiple sound files simultaneously is not supported.

All server requests that have a JSON-encoded body must set the `Content-Type` header to
`application/json`. 

All server responses are JSON objects of the form:
```
{
  "data": {
    ...request specific response data...
    }
  }, 
  "status": 200
}
```

The "status" attribute is the same as, and has the same semantics as, the HTTP response status.

The following are the current Elpis API functions:

## dataset

### api/dataset/new

Create a new dataset - i.e. a collection of uploaded recordings/transcripts for training
models on.

**method:** POST

**body:** JSON object with the following attributes:

 - **name** (string): The name of the new dataset.

**response:** A JSON object something like:  
```json
{
  "data": {
    "config": {
      "date": "1584103126.6183221", 
      "files": [], 
      "has_been_processed": false, 
      "hash": "875b333a9fedbe5b22520e3c7127cbee", 
      "logger": null, 
      "name": "ds", 
      "tier": "Phrase"
    }
  }, 
  "status": 200
}
```

### api/dataset/list

List current datasets.

**method:** GET

**response:** A JSON object something like:  
```json
{
  "data": {
    "list": [
      "ds", 
      "ds2"
    ]
  }, 
  "status": 200
}
```

The "list" property is a list of datasets.

### api/dataset/load

Start using an existing dataset.

**method:** POST

**body:** JSON object with the following attributes:

 - **name** (string): The name of the existing dataset.

**response:** A JSON object something like:  
```json
{
  "data": {
    "config": {
      "date": "1584105467.128971", 
      "files": [
        "1_1_1.eaf", 
        "1_1_1.wav", 
        "1_1_2.eaf", 
        "1_1_2.wav", 
        "1_1_3.eaf", 
        "1_1_3.wav", 
        "1_1_4.eaf", 
        "1_1_4.wav"
      ], 
      "has_been_processed": true, 
      "hash": "5eae8c85d1fe086996619db7b5cb4985", 
      "logger": 0, 
      "name": "ds", 
      "tier": "Phrase"
    }
  }, 
  "status": 200
}```

### api/dataset/settings

Define dataset settings.

**method:** POST

**body:** JSON object with the following attributes:

 - **tier** (string) : The name of the ELAN tier that contains the transcript

**response:** A JSON object something like:  
```json
{
  "data": {
    "tier": "Phrase"
  }, 
  "status": 200
}
```

### api/dataset/files

Upload transcript/audio files into the dataset.

**method:** POST with Content-Type: multipart/form-data
**parameters:**

 - **file** (file) : The file to upload (this parameter may be repeated), which may be a
     wav audio file or an ELAN .eaf transcript.

**NB** The 'name' passed with the file must not be the full path of the file on the local system.

Training requires pairs of audio/transcript (.wav/.eaf) files.

**response:** A JSON object something like:  
```json
{
  "data": {
    "files": [
      "1_1_1.eaf"
    ]
  }, 
  "status": 200
}
```

The "list" property is a list of all files uploaded.

### api/dataset/prepare

Process the transcripts to create word/frequency lists.

**method:** POST

**response:** A JSON object something like:  
```json
{
  "data": {
    "wordlist": "{\"amakaang\": 1, \"di\": 1, \"kaai\": 1, \"hada\": 1, \"muila\": 3, \"dining\": 2, \"ayoku\": 2, \"kamar\": 2, \"mia\": 2, \"mui\": 1, \"hekaai\": 2, \"deina\": 2, \"del\": 1, \"ong\": 1, \"hayei\": 1, \"ba\": 1, \"hepikaai\": 1, \"botol\": 1, \"homi\": 1, \"dong\": 1, \"yaari\": 1}"
  }, 
  "status": 200
}
```

The "wordlist" property is a string-escaped JSON-encoded map of word orthographies to
their frequencies in the uploaded transcripts.

## pron-dict

### api/pron-dict/new

Create a new pronunciation dictionary.

**method:** POST

**body:** JSON object with the following attributes:

 - **name** (string) : The name of the new pronunciation dictionary.
 - **dataset_name** (string) : The name of the dataset.

**response:** A JSON object something like:  
```json
{
  "data": {
    "config": {
      "dataset_name": "ds", 
      "date": "1584103964.921259", 
      "hash": "ac5e6d899088a16cdb563b9a358f6749", 
      "l2s": null, 
      "logger": null, 
      "name": "pd"
    }
  }, 
  "status": 200
}
```

### api/pron-dict/load

Start using an existing pronunciation dictionary.

**method:** POST

**body:** JSON object with the following attributes:

 - **name** (string) : the name of the pronunciation dictionary

**response:** A JSON object something like:  
```json
{
  "data": {
    "config": {
      "dataset_name": "ds", 
      "date": "1584103964.921259", 
      "hash": "ac5e6d899088a16cdb563b9a358f6749", 
      "l2s": null, 
      "logger": null, 
      "name": "pd"
    }
  }, 
  "status": 200
}
```

### api/pron-dict/l2s

Specify the letter-to-sound mapping for creating a pronunciation dictionary.

**method:** POST with Content-Type: multipart/form-data
**parameters:**

 - **file** (file) : The plain-text file to upload.

The letter to sound file is used to build a pronunciation dictionary for the corpus. Make
one by listing one column of all the characters in your corpus. Make a second column
(separated by a space) of a symbol representing how that character is pronounced. You can
use IPA, or SAMPA for the pronunciation symbols. You can include comments in this file by
beginning the comment line with #. For example:  

```
# This is a comment 
n n
ng ŋ
r r
y j
```

**response:** A JSON object something like:  
```json
{
  "data": {
    "l2s": "# This is a comment\nn n\nng ŋ\nr r\ny j"
  }, 
  "status": 200
}
```

The "l2s" attribute is a copy of the uploaded file.

### api/pron-dict/generate-lexicon

Generate the pronunciation dictionary - i.e. a list of all words in the uploaded
transcripts, followed by their pronunciations as generated by combining the word spelling
with the letter-to-sound mapping uploaded with *api/pron-dict/l2s*

**method:** GET

**response:** A JSON object something like:  
```json
{
  "data": {
    "lexicon": "...text content..."
  }, 
  "status": 200
}
```

The "lexicon" attribute contains the generated text of the pronunciation dictionary.

### api/pron-dict/save-lexicon

Update the pronunciation dictionary.

**method:** POST

**body:** JSON object with the following attributes:
{"lexicon":"...text content..."}

The "lexicon" is list of all words in the uploaded transcripts, followed by their
pronunciations; i.e. an edited version of the pronunciation dictionary generated by
*api/pron-dict/generate-lexicon*

Requirements:

 - The lexicon content must end with a blank line.
 - The pronunciations are specified as space-separated phoneme symbols.
 - The pronunciations must use only phoneme symbols that were already specified in the
    letter-to-sound mapping uploaded to *api/pron-dict/l2s*

**response:** A JSON object something like:  
```json
{
  "data": {
    "lexicon": "...text content..."
  }, 
  "status": 200
}
```

The "lexicon" attribute is a copy of what was send in the request body.

### api/pron-dict/list

List the current pronunciation dictionaries.

**method:** GET

**response:** A JSON object something like:  
```json
{
  "data": {
    "list": [
      {
        "dataset_name": "ds", 
        "name": "pd"
      }
    ]
  }, 
  "status": 200
}
```

## model

### api/model/list

List the current models.

**method:** GET

**response:** A JSON object something like:  
```json
{
  "data": {
    "list": [
      {
        "dataset_name": "ds", 
        "name": "m", 
        "pron_dict_name": "pd", 
        "results": {}
      }
    ]
  }, 
  "status": 200
}
```

Before creating any models, the "list" attribute is an empty array. 

### api/model/new

Create a new model for training.

**method:** POST

**body:** JSON object with the following attributes:

 - **name** (string) : The name of the model to create.
 - **pron_dict_name** (string) : The pronunciation dictionary to use.

**response:** A JSON object something like:  
```json
{
  "data": {
    "config": {
      "dataset_name": "ds", 
      "date": "1584104595.007796", 
      "hash": "4be5606021330613a773244921fd91bd", 
      "logger": null, 
      "name": "m", 
      "ngram": 1, 
      "pron_dict_name": "pd", 
      "status": "untrained"
    }
  }, 
  "status": 200
}
```

### api/model/load

Start using an existing model.

**method:** POST

**body:** JSON object with the following attributes:

 - **name** (string) : The name of the existing model to use.

**response:** A JSON object something like:  
```json
{
  "data": {
    "config": {
      "dataset_name": "ds", 
      "date": "1584104595.007796", 
      "hash": "4be5606021330613a773244921fd91bd", 
      "logger": null, 
      "name": "m", 
      "ngram": 1, 
      "pron_dict_name": "pd", 
      "status": "untrained"
    }
  }, 
  "status": 200
}
```

### api/model/settings

Specify model configuration.

**method:** POST

**body:** JSON object with the following attributes:

 - **ngram** (string) : The n-gram setting (number of consecutive words to use) for the
     language model. 

**response:** A JSON object something like:  
```json
{
  "data": {
    "settings": {
      "ngram": 3
    }
  }, 
  "status": 200
}
```

### api/model/train

Start the process of training models on the dataset.

**method:** GET

**response:** A JSON object something like:  
```json
{
  "data": {
    "status": "training"
  }, 
  "status": 200
}
```

### api/model/status

Get the current status of a model being trained.

**method:** GET

**response:** A JSON object something like:  
```json
{
  "data": {
    "status": "training"
  }, 
  "status": 200
}
```

The "status" value may be:

 - "training" during training, or
 - "trained" after training has completed.

### api/model/results

Get the training results - i.e. metrics for the final model performance after training.

**method:** GET

**response:** A JSON object something like:  
```json
{
  "data": {
    "results": {
      "count_val": "5 / 5", 
      "del_val": "2", 
      "ins_val": "0", 
      "sub_val": "3", 
      "wer": "100.00"
    }
  }, 
  "status": 200
}
```

## transcription

### api/transcription/new

Upload an audio file to transcribe.

**method:** POST with Content-Type: multipart/form-data
**parameters:**

 - **file** (file) : The wav audio file to upload.

**response:** A JSON object something like:  
```json
{
  "data": {
    "originalFilename": "audio.wav", 
    "status": "ready"
  }, 
  "status": 200
}
```

### api/transcription/transcribe

Begin the transcription process, with the last recording uploaded using *api/transcription/new*.

**method:** GET

**response:** A JSON object something like:  
```json
{
  "data": {
    "status": "transcribed"
  }, 
  "status": 200
}
```

### api/transcription/status

Get the current status of the transcription process.

**method:** GET

**response:** A JSON object something like:  
```json
{
  "data": {
    "status": "transcribing", 
    "type": null
  }, 
  "status": 200
}
```

The "status" attribute may be:

 - "transcribing" while the audio file is still being transcribed, or
 - "trained" after transcription has completed.

### api/transcription/text

Get the plain-text version of the transcript.

**method:** GET

**response:** The (plain) text of the transcript.

### api/transcription/elan

Get the ELAN (.eaf) XML version of the transcript.

**method:** GET

**response:** The ELAN (.eaf) transcription, which has a tier that includes an aligned
  annotation for each word token.

## config

### config/reset

Reset the server - delete all uploads, datasets, pronunciation maps, etc.

**method:** POST

**response:** none

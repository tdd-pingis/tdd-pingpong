function createEditor(editorId, textareaId) {
  let editor = ace.edit(editorId);
  let textarea = document.getElementById(textareaId);

  editor.setTheme("ace/theme/monokai");
  editor.getSession().setMode("ace/mode/java");
  editor.getSession().on("change", () => {
    textarea.value = editor.getSession().getValue();
  });
  return editor;
}

createEditor("submission-editor", "submission-code");
staticEditor = createEditor("static-editor", "static-code");
staticEditor.setReadOnly(true);

function clicked(event) {
  if (!confirm("Are you ready to submit?")) {
    event.preventDefault();
  }
}
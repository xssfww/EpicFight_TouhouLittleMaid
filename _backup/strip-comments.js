const fs = require('fs');
const path = require('path');

const ROOT = 'C:/Users/Administrator/EF_TLM/src/main/java';
const APPLY = process.argv.includes('--apply');
const DIFF = process.argv.includes('--diff');

function findComments(src) {
  const spans = [];
  const n = src.length;
  let i = 0;
  while (i < n) {
    const c = src[i];
    if (c === '"') {
      if (src.startsWith('"""', i)) {
        i += 3;
        while (i < n) {
          if (src[i] === '\\') { i += 2; continue; }
          if (src.startsWith('"""', i)) { i += 3; break; }
          i++;
        }
        continue;
      }
      i++;
      while (i < n) {
        if (src[i] === '\\') { i += 2; continue; }
        if (src[i] === '"') { i++; break; }
        if (src[i] === '\n') { break; }
        i++;
      }
      continue;
    }
    if (c === "'") {
      i++;
      while (i < n) {
        if (src[i] === '\\') { i += 2; continue; }
        if (src[i] === "'") { i++; break; }
        if (src[i] === '\n') { break; }
        i++;
      }
      continue;
    }
    if (c === '/' && src[i + 1] === '/') {
      const start = i;
      while (i < n && src[i] !== '\n') i++;
      spans.push({ start, end: i, kind: 'line' });
      continue;
    }
    if (c === '/' && src[i + 1] === '*') {
      const start = i;
      i += 2;
      while (i < n && !(src[i] === '*' && src[i + 1] === '/')) i++;
      i = Math.min(n, i + 2);
      spans.push({ start, end: i, kind: 'block' });
      continue;
    }
    i++;
  }
  return spans;
}

function strip(src) {
  const n = src.length;
  const spans = findComments(src);
  let out = '';
  let pos = 0;
  let removedLines = 0;
  for (const span of spans) {
    const lineStart = src.lastIndexOf('\n', span.start - 1) + 1;
    const before = src.slice(lineStart, span.start);
    const nl = src.indexOf('\n', span.end);
    const lineEnd = nl === -1 ? n : nl;
    const after = src.slice(span.end, lineEnd);
    if (before.trim() === '' && after.trim() === '') {
      out += src.slice(pos, lineStart);
      pos = nl === -1 ? n : nl + 1;
      removedLines += src.slice(lineStart, pos).split('\n').length - 1;
    } else {
      out += src.slice(pos, span.start);
      if (before.trim() !== '' && after.trim() !== '') out += ' ';
      pos = span.end;
    }
  }
  out += src.slice(pos);
  return { text: out, removedLines };
}

function normalize(text, eol) {
  let t = text.replace(/\r\n/g, '\n');
  t = t.replace(/[ \t]+\n/g, '\n');
  t = t.replace(/\n{3,}/g, '\n\n');
  t = t.replace(/^\n+/, '');
  t = t.replace(/\n+$/, '');
  t += '\n';
  if (eol === '\r\n') t = t.replace(/\n/g, '\r\n');
  return t;
}

function transform(raw) {
  const bom = raw.charCodeAt(0) === 0xFEFF ? '\uFEFF' : '';
  const body = bom ? raw.slice(1) : raw;
  const eol = body.includes('\r\n') ? '\r\n' : '\n';
  const commentCount = findComments(body).length;
  const { text, removedLines } = strip(body);
  return { next: bom + normalize(text, eol), commentCount, removedLines, eol };
}

function walk(dir) {
  const out = [];
  for (const e of fs.readdirSync(dir, { withFileTypes: true })) {
    const p = path.join(dir, e.name);
    if (e.isDirectory()) out.push(...walk(p));
    else if (e.name.endsWith('.java')) out.push(p);
  }
  return out;
}

if (DIFF) {
  const target = process.argv[process.argv.length - 1];
  const f = path.isAbsolute(target) ? target : path.join(ROOT, target);
  const raw = fs.readFileSync(f, 'utf8');
  const { next } = transform(raw);
  const a = raw.split(/\r\n|\n/);
  const b = next.split(/\r\n|\n/);
  let i = 0, j = 0, hunks = 0;
  while (i < a.length || j < b.length) {
    if (a[i] === b[j]) { i++; j++; continue; }
    let k = 1;
    while (k < 60 && (i + k < a.length || j + k < b.length) && a[i + k] !== b[j + k]) k++;
    if (i + k > a.length && j + k > b.length) k = Math.max(a.length - i, b.length - j);
    console.log(`@@ orig line ${i + 1} -> new line ${j + 1}`);
    for (let x = 0; x < k && i + x < a.length; x++) console.log(`- ${i + x + 1}: ${JSON.stringify(a[i + x])}`);
    for (let x = 0; x < k && j + x < b.length; x++) console.log(`+ ${j + x + 1}: ${JSON.stringify(b[j + x])}`);
    i += k; j += k; hunks++;
    if (hunks > 25) { console.log('... (truncated)'); break; }
  }
  process.exit(0);
}

const files = walk(ROOT).sort();
let changed = 0;
let removedLinesTotal = 0;
let removedCommentCount = 0;
const problems = [];
for (const f of files) {
  const raw = fs.readFileSync(f, 'utf8');
  if (raw.includes('\uFFFD')) { problems.push(`BAD-ENCODING ${f}`); continue; }
  const { next, commentCount, removedLines } = transform(raw);
  if (next !== raw) {
    changed++;
    removedLinesTotal += removedLines;
    removedCommentCount += commentCount;
    if (APPLY) fs.writeFileSync(f, next, 'utf8');
    else console.log(`would-change ${path.relative(ROOT, f)} comments=${commentCount} lines=${removedLines}`);
  }
}
console.log(`${APPLY ? 'APPLIED' : 'DRY-RUN'}: files=${files.length} changed=${changed} commentsRemoved=${removedCommentCount} commentOnlyLinesRemoved=${removedLinesTotal}`);
for (const p of problems) console.log(p);

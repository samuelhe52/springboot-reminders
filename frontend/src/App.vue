<template>
  <router-view />
</template>

<style>
:root {
  --app-text: #0f172a;
  --app-muted: #64748b;
  --app-bg-start: #eef4ff;
  --app-bg-end: #f8fafc;
  --app-panel-bg: rgba(255, 255, 255, 0.9);
  --app-panel-border: rgba(148, 163, 184, 0.3);
  --app-shadow:
    0 20px 38px rgba(15, 23, 42, 0.08),
    0 6px 12px rgba(15, 23, 42, 0.08);
}

html.dark {
  --app-text: #f1f5f9;
  --app-muted: #94a3b8;
  --app-bg-start: #0b1220;
  --app-bg-end: #111827;
  --app-panel-bg: rgba(17, 24, 39, 0.82);
  --app-panel-border: rgba(71, 85, 105, 0.42);
  --app-shadow:
    0 20px 38px rgba(2, 6, 23, 0.55),
    0 6px 12px rgba(2, 6, 23, 0.5);
}

*,
*::before,
*::after {
  box-sizing: border-box;
}

html,
body,
#app {
  min-height: 100%;
}

body {
  margin: 0;
  color: var(--app-text);
  font-family:
    "Noto Sans SC",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
  background: var(--app-bg-start);
}
</style>

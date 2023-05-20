import fs from "fs";
import fetch from "node-fetch";
import FormData from "form-data";
import core from "@actions/core";
import github from "@actions/github";

const path = core.getInput("path"),
    webhookUrl = core.getInput("webhook_url"),
    { runId, ref, repo } = github.context,
    formData = new FormData();

const files = fs
    .readdirSync(path)
    .filter((x) => x.endsWith(".jar") && fs.statSync(path + x).isFile())
    .map((x) => [x, fs.readFileSync(path + x)]);

const payload = {
    embeds: [
        {
            description: `Run [${runId}](https://github.com/${repo.owner}/${repo.repo}/actions/runs/${runId}) produced **${files.length}** artifacts.\n\n\`${repo.owner}/${repo.repo}:${ref}\``,
            color: 0x22c55e,
        },
    ],
};

formData.append("payload_json", JSON.stringify(payload));

files.forEach(([filename, file], i) => {
    formData.append(`files[${i}]`, file, filename);
});

await fetch(webhookUrl, {
    method: "POST",
    body: formData,
});

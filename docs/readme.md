# Project CardBoard Documentation

## Development

All the pages are in `/docs` and are written in [MDX](https://mdxjs.com/).
Modify those to whatever you need. Any changes to the structure of the site and adding new docs will involve changing files in `/src`.

Start a local development server to view your changes live:

```sh
npm start
```

Once you finished with your changes, make a branch and commit:

```sh
git checkout -b my-changes
git commit -m "Added new getting started documentation."
git push -u origin my-changes
```

Then open a PR from `my-changes` to `master`. Once your PR is merged, the docs site will automatically deploy to https://duelers.github.io/Duelers.

const gulp = require("gulp");
const merge2 = require("merge2");
const concat = require("gulp-concat");
const rename = require("gulp-rename");
const uglify = require("gulp-uglify");
const gzip = require("gulp-gzip");
const clean = require("gulp-clean");


const BUNDLES = require("./bundles");

const BUNDLE_OUTPUT_PATH = "dist";

const GZIP_ENABLED = false;

const OMP_SOURCES = [
    "src/**/*",
    "dist/*.js"
];

const VENDOR_SOURCES = [
    "vendor/**/*"

    // "!vendor/ext-4.0.7/build{,/**}",
];

const CLEAN_DEST_FILES = [
    "../src/main/resources/public/js/*",
    "../src/main/resources/public/vendor/*",
    "!.gitkeep"
];


gulp.task("clean", () => {
    gulp.src(CLEAN_DEST_FILES)
        .pipe(clean({force: true}))
});

gulp.task("copyOmpLibraries", ["bundleOmpLibraries"], () => {
    gulp.src(OMP_SOURCES)
        .pipe(gulp.dest("../src/main/resources/public/js"));
});

gulp.task("copyVendorLibraries", () => {
    gulp.src(VENDOR_SOURCES)
        .pipe(gulp.dest("../src/main/resources/public/vendor/"));
});

gulp.task("bundleOmpLibraries", () => {
    let bundleNames = Object.keys(BUNDLES);

    let streams = bundleNames.map(bundleName => {
        let stream =
            gulp.src(BUNDLES[bundleName])
                .pipe(concat(`${bundleName}.js`))
                .pipe(gulp.dest(BUNDLE_OUTPUT_PATH))
                .pipe(rename(`${bundleName}.min.js`))
                .pipe(uglify())
                .pipe(gulp.dest(BUNDLE_OUTPUT_PATH));

        if (!GZIP_ENABLED) return stream;

        return stream
            .pipe(rename(`${bundleName}.gz.js`))
            .pipe(gzip({
                append: false,
                gzipOptions: {level: 9}
            }))
            .pipe(gulp.dest(BUNDLE_OUTPUT_PATH));
    });

    return merge2(streams)
});

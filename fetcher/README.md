# Fetcher

The fetcher module is responsible for fetching XKCD comic information and
streaming it to the projection service. It uses the outbox pattern to ensure
that the data is streamed reliably and consistently.

The fetcher relies on the XKCD API to fetch comic information. The endpoints
used by the fetcher are:

- `https://xkcd.com/info.0.json` - Fetches the latest XKCD comic information.
- `https://xkcd.com/{comic_number}/info.0.json` - Fetches information about a
  specific XKCD comic.

The fetcher will fetch information from XKCD and stream it to the projection
by using [Debezium](https://debezium.io) to capture changes in the database and
stream them to the projection service.
